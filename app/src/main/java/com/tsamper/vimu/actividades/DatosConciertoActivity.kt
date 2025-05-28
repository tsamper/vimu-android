package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton

import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager

import com.bumptech.glide.Glide
import com.tsamper.vimu.R
import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.adaptadores.ConciertoAdapter
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.Concierto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DatosConciertoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos_concierto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val idConcierto = intent.getIntExtra("idConcierto", 0)
        val idUsuario = intent.getIntExtra("idUsuario", 0)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val tituloConcierto: TextView = findViewById(R.id.tituloConcierto)
        val perfilButton: ImageButton = findViewById(R.id.profileButton)
        perfilButton.setOnClickListener{
            val intent = Intent(this@DatosConciertoActivity, PerfilActivity::class.java).apply {
                putExtra("idUsuario", idUsuario)
                putExtra("tipoUsuario", tipoUsuario)
            }
            startActivity(intent)
        }
        val grupo: TextView = findViewById(R.id.grupoText)
        val fecha: TextView = findViewById(R.id.fechaText)
        val hora: TextView = findViewById(R.id.horaText)
        val recinto: TextView = findViewById(R.id.recintoText)
        val ciudad: TextView = findViewById(R.id.ciudadText)
        val imagen: ImageView = findViewById(R.id.conciertoImage)
        val spinnerNormales: Spinner = findViewById(R.id.spinnerNormales)
        val spinnerVips: Spinner = findViewById(R.id.spinnerVips)
        val apiService = RetrofitClient.getApiService()
        apiService.obtenerConciertoPorId(idConcierto).enqueue(object : Callback<Concierto> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<Concierto>, response: Response<Concierto>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                        tituloConcierto.setText(it.nombre)

                        grupo.setText("🎤 ${it.grupo.nombre}")
                        fecha.setText("📅 ${it.fecha}")
                        hora.setText("⏰ ${it.hora}")
                        recinto.setText("🏟️ ${it.recinto!!.nombre}")
                        ciudad.setText("📍 ${it.recinto.ciudad}")
                        val imagenUrl = VariablesGlobales.conexion + "/" + it.imagen // ejemplo: "img/carteles/Hoke_Murcia.jpg"

                        Glide.with(this@DatosConciertoActivity)
                            .load(imagenUrl)
                            .into(imagen)

                        val maxNormales = it.cantidadEntradas - it.cantidadEntradasVendidas
                        val maxVips = it.cantidadEntradasVip - it.cantidadEntradasVipVendidas
                        val opcionesNormales = getOpcionesCantidad(maxNormales)
                        val opcionesVips = getOpcionesCantidad(maxVips)


                        val adapterNormales = ArrayAdapter(this@DatosConciertoActivity, android.R.layout.simple_spinner_item, opcionesNormales)
                        adapterNormales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerNormales.adapter = adapterNormales

                        val adapterVips = ArrayAdapter(this@DatosConciertoActivity, android.R.layout.simple_spinner_item, opcionesVips)
                        adapterVips.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerVips.adapter = adapterVips
                    }
                } else {
                    Log.d("API", "ERROR: " + response.message())
                }
            }
            override fun onFailure(call: Call<Concierto>, t: Throwable) {
                Log.d("API", "Fallo llamada a la API: " + t.message)
                Toast.makeText(this@DatosConciertoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        val enviarButtton: Button = findViewById(R.id.btnEnviar)
        enviarButtton.setOnClickListener{
            apiService.comprarEntradas(idConcierto, idUsuario, spinnerNormales.selectedItem.toString().toInt(), spinnerVips.selectedItem.toString().toInt()).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DatosConciertoActivity, "Compra realizada con éxito", Toast.LENGTH_SHORT).show()
                        // Aquí puedes actualizar UI o recargar datos
                    } else {
                        Toast.makeText(this@DatosConciertoActivity, "Error en la compra", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@DatosConciertoActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun getOpcionesCantidad(maxEntradas: Int): List<Int> {
        return (0..minOf(maxEntradas, 10)).toList()
    }
}