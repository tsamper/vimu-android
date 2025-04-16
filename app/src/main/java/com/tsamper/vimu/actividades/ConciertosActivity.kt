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
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val conciertos: ArrayList<Concierto> = ArrayList()
        val apiService = RetrofitClient.getApiService()
        apiService.obtenerConciertos().enqueue(object : Callback<ArrayList<Concierto>> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<ArrayList<Concierto>>, response: Response<ArrayList<Concierto>>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                        conciertos.addAll(it)
                        Log.d("CONCIERTOS", conciertos.toString())
                        recyclerView.layoutManager = GridLayoutManager(this@ConciertosActivity, 2)
                        adapter = ConciertoAdapter(conciertos) { concierto ->
                            val intent = Intent(this@ConciertosActivity, DatosConciertoActivity::class.java).apply {
                               putExtra("idConcierto", concierto.id)
                            }
                            startActivity(intent)
                        }
                        recyclerView.adapter = adapter
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