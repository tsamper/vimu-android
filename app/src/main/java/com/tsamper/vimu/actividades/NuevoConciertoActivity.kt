package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tsamper.vimu.R
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.Concierto
import com.tsamper.vimu.modelo.Grupo
import com.tsamper.vimu.modelo.Recinto
import com.tsamper.vimu.modelo.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NuevoConciertoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nuevo_concierto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val apiService = RetrofitClient.getApiService()
        val idUsuario = intent.getIntExtra("idUsuario", 0)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val grupoSpinner: Spinner = findViewById(R.id.grupoSpinner)
        val recintoSpinner: Spinner = findViewById(R.id.recintoSpinner)
        var nombreConcierto = ""
        val fecha: EditText = findViewById(R.id.fecha)
        val hora: EditText = findViewById(R.id.hora)
        val cantidadEntradas: EditText = findViewById(R.id.cantidadEntradas)
        val precioEntradas: EditText = findViewById(R.id.precioEntradas)
        val cantidadEntradasVip: EditText = findViewById(R.id.cantidadEntradasVip)
        val precioEntradasVip: EditText = findViewById(R.id.precioEntradasVip)
        val registrarBtn: Button = findViewById(R.id.registrarBtn)
        val grupos: ArrayList<String> = ArrayList()
        val recintos: ArrayList<String> = ArrayList()
        apiService.obtenerGrupos().enqueue(object : Callback<List<Grupo>> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<List<Grupo>>, response: Response<List<Grupo>>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (grupo in it){
                            grupos.add(grupo.nombre)
                        }
                        val grupoAdapter = ArrayAdapter(this@NuevoConciertoActivity, android.R.layout.simple_spinner_item, grupos)
                        grupoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        grupoSpinner.adapter = grupoAdapter
                    }
                } else {
                    Log.d("API", "ERROR: " + response.message())
                }
            }

            override fun onFailure(call: Call<List<Grupo>>, t: Throwable) {
                Log.d("API", "Fallo llamada a la API: " + t.message)
                Toast.makeText(this@NuevoConciertoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        apiService.obtenerRecintos().enqueue(object : Callback<List<Recinto>> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<List<Recinto>>, response: Response<List<Recinto>>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (recinto in it){
                            recintos.add(recinto.nombre)
                        }
                        val recintoAdapter = ArrayAdapter(this@NuevoConciertoActivity, android.R.layout.simple_spinner_item, recintos)
                        recintoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        recintoSpinner.adapter = recintoAdapter
                    }
                } else {
                    Log.d("API", "ERROR: " + response.message())
                }
            }

            override fun onFailure(call: Call<List<Recinto>>, t: Throwable) {
                Log.d("API", "Fallo llamada a la API: " + t.message)
                Toast.makeText(this@NuevoConciertoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        val recintoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, recintos)
        recintoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        recintoSpinner.adapter = recintoAdapter
        fun actualizarNombreConcierto() {
            val grupoSeleccionado = grupoSpinner.selectedItem?.toString() ?: ""
            val recintoSeleccionado = recintoSpinner.selectedItem?.toString() ?: ""

            if (grupoSeleccionado.isNotEmpty() && recintoSeleccionado.isNotEmpty()) {
                val nombre = "$grupoSeleccionado en $recintoSeleccionado"
                nombreConcierto  = nombre
            }
        }
        grupoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                actualizarNombreConcierto()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        recintoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                actualizarNombreConcierto()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        registrarBtn.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val recinto: Recinto = apiService.obtenerRecintoPorNombre(recintoSpinner.selectedItem?.toString()!!)
                    val usuario: Usuario = apiService.obtenerUsuarioPorIdSuspend(idUsuario)
                    val grupo: Grupo = apiService.obtenerGrupoPorNombre(grupoSpinner.selectedItem?.toString()!!)
                    val concierto = Concierto(
                        0,
                        nombreConcierto,
                        "img/carteles/ejemplo.jpg",
                        recinto,
                        fecha.text.toString(),
                        hora.text.toString(),
                        cantidadEntradasVip.text.toString().toInt(),
                        0,
                        precioEntradasVip.text.toString().toDouble(),
                        cantidadEntradas.text.toString().toInt(),
                        0,
                        precioEntradas.text.toString().toDouble(),
                        usuario,
                        grupo
                        )
                    val response = apiService.registrarConcierto(concierto, idUsuario)
                    if (response.isSuccessful) {
                        val intent = Intent(this@NuevoConciertoActivity, ConciertosActivity::class.java).apply {
                            putExtra("idUsuario", idUsuario)
                            putExtra("tipoUsuario", tipoUsuario)
                        }
                        startActivity(intent)
                    } else {
                        Log.d("API", "ERROR: ${response.message()}")
                    }
                }catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("API", "Error: ${e.message}")

                    }
                }
            }
        }
    }
}