package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tsamper.vimu.R
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
        val apiService = RetrofitClient.getApiService()
        apiService.obtenerConciertoPorId(idConcierto).enqueue(object : Callback<Concierto> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<Concierto>, response: Response<Concierto>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {

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
    }
}