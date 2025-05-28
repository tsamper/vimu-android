package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.tsamper.vimu.R
import com.tsamper.vimu.VariablesGlobales
import com.tsamper.vimu.adaptadores.ConciertoAdapter
import com.tsamper.vimu.adaptadores.EntradaConciertoAdapter
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.Concierto
import com.tsamper.vimu.modelo.EntradaConcierto
import com.tsamper.vimu.modelo.EntradaConciertoGrouped
import com.tsamper.vimu.modelo.Usuario
import com.tsamper.vimu.modelo.enums.Privilegios
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilActivity : AppCompatActivity() {
    private lateinit var adapter: ConciertoAdapter

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
        val apellidos: TextView = findViewById(R.id.apellidosTextView)
        val email: TextView = findViewById(R.id.emailTextView)
        val tipo: TextView = findViewById(R.id.tipoTextView)
        val idUser = intent.getIntExtra("idUsuario", 0)
        val tipoUser = intent.getStringExtra("tipoUsuario")
        val tabLayout = findViewById<TabLayout>(R.id.perfilTabLayout)
        val entradasRV = findViewById<RecyclerView>(R.id.entradasRecyclerView)
        val favoritosRV = findViewById<RecyclerView>(R.id.favoritosRecyclerView)
        val apiService = RetrofitClient.getApiService()

        entradasRV.layoutManager = LinearLayoutManager(this)
        entradasRV.adapter = EntradaConciertoAdapter(emptyList(), this)

        favoritosRV.layoutManager = GridLayoutManager(this, 2)
        val conciertos: ArrayList<Concierto> = ArrayList()
        adapter = ConciertoAdapter(conciertos) { concierto ->
            val intent = Intent(this@PerfilActivity, DatosConciertoActivity::class.java).apply {
                putExtra("idConcierto", concierto.id)
                putExtra("idUsuario", idUser)
                putExtra("tipoUsuario", tipoUser)
            }
            startActivity(intent)
        }
        favoritosRV.adapter = adapter
        apiService.obtenerEntradasUsuario(idUser).enqueue(object : Callback<Map<String, List<EntradaConcierto>>> {
            override fun onResponse(
                call: Call<Map<String, List<EntradaConcierto>>>,
                response: Response<Map<String, List<EntradaConcierto>>>
            ) {
                if (response.isSuccessful) {
                    val mapa = response.body()
                    if (mapa != null) {
                        mostrarEntradasDelUsuario(mapa)
                    }
                } else {
                    Log.e("PerfilActivity", "Error en respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Map<String, List<EntradaConcierto>>>, t: Throwable) {
                Log.e("PerfilActivity", "Error de red: ${t.localizedMessage}", t)
            }
        })
        apiService.obtenerConciertosGuardados(idUser).enqueue(object : Callback<ArrayList<Concierto>> {
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
                Toast.makeText(this@PerfilActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        // Añade las pestañas
        if (tipoUser == "USER") {
            tabLayout.addTab(tabLayout.newTab().setText("Entradas"))
            tabLayout.addTab(tabLayout.newTab().setText("Favoritos"))

            // Manejador de selección de pestañas
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            entradasRV.visibility = View.VISIBLE
                            favoritosRV.visibility = View.GONE
                        }

                        1 -> {
                            entradasRV.visibility = View.GONE
                            favoritosRV.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }else{
            tabLayout.addTab(tabLayout.newTab().setText("Mis conciertos"))
        }
        val cerrarSesionButton: Button = findViewById(R.id.cerrarSesionButton)
        cerrarSesionButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        apiService.obtenerUsuarioPorId(idUser).enqueue(object : Callback<Usuario> {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                Log.d("API", "Accediendo llamada a la API")
                if (response.isSuccessful) {
                    response.body()?.let {
                        nombre.setText("Nombre: ${it.nombre}")
                        apellidos.setText("Apellidos: ${it.apellidos}")
                        email.setText("Email: ${it.email}")
                        tipo.setText("Tipo de cuenta: ${tipoUser}")

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

    fun mostrarEntradasDelUsuario(entradaMap: Map<String, List<EntradaConcierto>>) {
        val agrupadas = entradaMap.mapNotNull { (_, entradas) ->
            val concierto = entradas.firstOrNull()?.concierto
            if (concierto != null) {
                EntradaConciertoGrouped(concierto, entradas.size)
            } else null
        }

        val adapter = EntradaConciertoAdapter(agrupadas, this) // `this` debe ser un Context (Activity o Fragment)
        val recyclerView = findViewById<RecyclerView>(R.id.entradasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}