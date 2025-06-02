package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tsamper.vimu.R
import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.Concierto
import com.tsamper.vimu.modelo.Grupo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.net.toUri

class GrupoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grupo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val idGrupo = intent.getIntExtra("idGrupo", 0)
        val imagen: ImageView = findViewById(R.id.grupoImage)
        val nombreGrupo: TextView = findViewById(R.id.nombreGrupo)
        val descripcion: TextView = findViewById(R.id.descripcionText)
        val genero: TextView = findViewById(R.id.generoText)
        val ciudad: TextView = findViewById(R.id.ciudadText)
        val pais: TextView = findViewById(R.id.paisText)
        val perfilSpotify: TextView = findViewById(R.id.perfilSpotifyText)
        var enlaceSpotify: String = ""
        val apiService = RetrofitClient.getApiService()
        apiService.obtenerGrupoPorId(idGrupo).enqueue(object : Callback<Grupo> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<Grupo>, response: Response<Grupo>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                        nombreGrupo.setText(it.nombre)

                        nombreGrupo.setText(" ${it.nombre}")
                        val descripcionText = SpannableStringBuilder().apply {
                            append("Descripción: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.descripcion}")
                        }
                        descripcion.text = descripcionText

                        val generoText = SpannableStringBuilder().apply {
                            append("Género: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.genero}")
                        }
                        genero.text = generoText

                        val ciudadText = SpannableStringBuilder().apply {
                            append("Ciudad: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.ciudad}")
                        }
                        ciudad.text = ciudadText

                        val paisText = SpannableStringBuilder().apply {
                            append("País: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.pais}")
                        }
                        pais.text = paisText
                        enlaceSpotify = it.perfilSpotify
                        val perfilText = SpannableStringBuilder().apply {
                            append("Perfil Spotify: ")
                            setSpan(StyleSpan(Typeface.BOLD), 0, length, 0)
                            append("${it.perfilSpotify}")
                        }
                        perfilSpotify.text = perfilText
                        val imagenUrl = VariablesGlobales.conexion + "/" + it.imagen

                        Glide.with(this@GrupoActivity)
                            .load(imagenUrl)
                            .into(imagen)
                    }
                } else {
                    Log.d("API", "ERROR: " + response.message())
                }
            }
            override fun onFailure(call: Call<Grupo>, t: Throwable) {
                Log.d("API", "Fallo llamada a la API: " + t.message)
                Toast.makeText(this@GrupoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        perfilSpotify.setOnClickListener{
            val spotifyUrl = enlaceSpotify
            try {
                // Primero intenta abrir con la app de Spotify
                val spotifyIntent = Intent(Intent.ACTION_VIEW, spotifyUrl.toUri())
                spotifyIntent.setPackage("com.spotify.music")
                startActivity(spotifyIntent)
            } catch (e: Exception) {
                // Si no tiene Spotify instalado, abre en el navegador
                val browserIntent = Intent(Intent.ACTION_VIEW, spotifyUrl.toUri())
                startActivity(browserIntent)
            }
        }
    }
}