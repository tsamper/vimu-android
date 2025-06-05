package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsamper.vimu.adaptadores.ConciertoAdapter
import com.tsamper.vimu.R
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.Concierto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConciertosActivity : AppCompatActivity() {

    private lateinit var adapter: ConciertoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conciertos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val idUser = intent.getIntExtra("idUsuario", 0)
        val tipoUser = intent.getStringExtra("tipoUsuario")
        val perfilButton: ImageButton = findViewById(R.id.profileButton)
        perfilButton.setOnClickListener{
            val intent = Intent(this@ConciertosActivity, PerfilActivity::class.java).apply {
                putExtra("idUsuario", idUser)
                putExtra("tipoUsuario", tipoUser)
            }
            startActivity(intent)
        }
        val nuevoConciertoBtn: Button = findViewById(R.id.nuevoConciertoBtn)
        if (tipoUser == "USER"){
            nuevoConciertoBtn.visibility = View.GONE
        }
        nuevoConciertoBtn.setOnClickListener{
            val intent = Intent(this@ConciertosActivity, NuevoConciertoActivity::class.java).apply {
                putExtra("idUsuario", idUser)
                putExtra("tipoUsuario", tipoUser)
            }
            startActivity(intent)
        }
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val conciertos: ArrayList<Concierto> = ArrayList()
        val apiService = RetrofitClient.getApiService()
        adapter = ConciertoAdapter(conciertos) { concierto ->
            val intent = Intent(this@ConciertosActivity, DatosConciertoActivity::class.java).apply {
                putExtra("idConcierto", concierto.id)
                putExtra("idUsuario", idUser)
                putExtra("tipoUsuario", tipoUser)
            }
            startActivity(intent)
        }
        recyclerView.layoutManager = GridLayoutManager(this@ConciertosActivity, 2)
        recyclerView.adapter = adapter
        apiService.obtenerConciertos().enqueue(object : Callback<ArrayList<Concierto>> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<ArrayList<Concierto>>, response: Response<ArrayList<Concierto>>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.actualizarConciertos(it)
                    }
                } else {
                    Log.d("API", "ERROR: " + response.message())
                }
            }

            override fun onFailure(call: Call<ArrayList<Concierto>>, t: Throwable) {
                Log.d("API", "Fallo llamada a la API: " + t.message)
                Toast.makeText(this@ConciertosActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        val buscarButton: Button = findViewById(R.id.searchButton)
        val buscarInput: EditText = findViewById(R.id.searchInput)
        val filtroSpinner: Spinner = findViewById(R.id.searchFilterSpinner)
        // Opciones del desplegable
        val filtros = arrayOf("Artista", "Ciudad")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, filtros)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filtroSpinner.adapter = adapterSpinner

        buscarButton.setOnClickListener{
            var filtro = ""
            if (filtroSpinner.selectedItem.toString().equals("Artista")) {
                filtro = "Artista"
            }else{
                filtro = "Ciudad"
            }
            apiService.buscarConciertos(filtro, buscarInput.text.toString()).enqueue(object : Callback<ArrayList<Concierto>> {
                @SuppressLint("NotifyDataSetChanged")
                @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
                override fun onResponse(call: Call<ArrayList<Concierto>>, response: Response<ArrayList<Concierto>>) {
                    Log.d("API", "Accediendo llamada a la API")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            adapter.actualizarConciertos(it)
                        }
                    } else {
                        Log.d("API", "ERROR: " + response.message())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Concierto>>, t: Throwable) {
                    Log.d("API", "Fallo llamada a la API: " + t.message)
                    Toast.makeText(this@ConciertosActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}