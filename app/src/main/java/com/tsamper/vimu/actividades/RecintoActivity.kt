package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tsamper.vimu.R
import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.Grupo
import com.tsamper.vimu.modelo.Recinto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecintoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recinto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val idRecinto = intent.getIntExtra("idRecinto", 0)
        val idUsuario = intent.getIntExtra("idUsuario", 0)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val nombreRecinto: TextView = findViewById(R.id.nombreRecinto)
        val direccion: TextView = findViewById(R.id.direccionText)
        val ciudad: TextView = findViewById(R.id.ciudadText)
        val telefono: TextView = findViewById(R.id.telefonoText)
        val correo: TextView = findViewById(R.id.correoText)
        val enlaceMaps: TextView = findViewById(R.id.enlaceMapsText)
        val logo: ImageView = findViewById(R.id.logoImage)
        logo.setOnClickListener{
            val intent = Intent(this@RecintoActivity, ConciertosActivity::class.java).apply {
                putExtra("idUsuario", idUsuario)
                putExtra("tipoUsuario", tipoUsuario)
            }
            startActivity(intent)
        }
        val perfilButton: ImageButton = findViewById(R.id.profileButton)
        perfilButton.setOnClickListener{
            val intent = Intent(this@RecintoActivity, PerfilActivity::class.java).apply {
                putExtra("idUsuario", idUsuario)
                putExtra("tipoUsuario", tipoUsuario)
            }
            startActivity(intent)
        }
        var maps: String = ""
        val apiService = RetrofitClient.getApiService()
        apiService.obtenerRecintoPorId(idRecinto).enqueue(object : Callback<Recinto> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<Recinto>, response: Response<Recinto>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                        nombreRecinto.setText(it.nombre)

                        nombreRecinto.setText(" ${it.nombre}")
                        val direccionText = SpannableStringBuilder().apply {
                            append("Dirección: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.direccion}")
                        }
                        direccion.text = direccionText

                        val ciudadText = SpannableStringBuilder().apply {
                            append("Ciudad: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.ciudad}")
                        }
                        ciudad.text = ciudadText

                        val telefonoText = SpannableStringBuilder().apply {
                            append("Teléfono: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.telefono}")
                        }
                        telefono.text = telefonoText

                        val correoText = SpannableStringBuilder().apply {
                            append("Correo: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.email}")
                        }
                        correo.text = correoText
                        val enlaceText = SpannableStringBuilder().apply {
                            append("Enlace Maps: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("Abrir en Maps")
                        }
                        maps = it.enlaceMaps
                        enlaceMaps.text = enlaceText
                    }
                } else {
                    Log.d("API", "ERROR: " + response.message())
                }
            }
            override fun onFailure(call: Call<Recinto>, t: Throwable) {
                Log.d("API", "Fallo llamada a la API: " + t.message)
                Toast.makeText(this@RecintoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        enlaceMaps.setOnClickListener{
            try {
                // Primero intenta abrir con la app de Google Maps
                val mapsIntent = Intent(Intent.ACTION_VIEW, maps.toUri())
                mapsIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapsIntent)
            } catch (e: Exception) {
                // Si no tiene Google Maps instalado, abre en el navegador
                val browserIntent = Intent(Intent.ACTION_VIEW, maps.toUri())
                startActivity(browserIntent)
            }
        }
    }
}