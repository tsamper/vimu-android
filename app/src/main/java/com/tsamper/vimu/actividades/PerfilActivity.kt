package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import com.tsamper.vimu.modelo.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nombre: TextView = findViewById(R.id.nombreTextView)
        val idUser = intent.getIntExtra("idUsuario", 0)
        val cerrarSesionButton: Button = findViewById(R.id.cerrarSesionButton)
        cerrarSesionButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val apiService = RetrofitClient.getApiService()
        apiService.obtenerUsuarioPorId(idUser).enqueue(object : Callback<Usuario> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                       nombre.setText(it.nombre)
                    }
                } else {
                    Log.d("API", "ERROR: " + response.message())
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.d("API", "Fallo llamada a la API: " + t.message)
                Toast.makeText(this@PerfilActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}