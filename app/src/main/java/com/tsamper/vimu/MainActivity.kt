package com.tsamper.vimu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.Ejemplo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val usuario: EditText = findViewById(R.id.usuario)
        val password: EditText = findViewById(R.id.password)
        val registro: TextView = findViewById(R.id.registro)
        val login: Button = findViewById(R.id.iniciarBtn)
        registro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {
            Log.d("eee", "INICIO")
            val apiService = RetrofitClient.getApiService("http://192.168.4.138:8080")
            apiService.ejemplo().enqueue(object : Callback<Ejemplo> {
                @SuppressLint("NotifyDataSetChanged")
                @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
                override fun onResponse(call: Call<Ejemplo>, response: Response<Ejemplo>) {
                    Log.d("API", "Accediendo llamada a la API")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Log.d("API", "Cuerpo de la respuesta: ${response.body()}")
                            Log.d("API", "" + it)
                        }
                    } else {
                        Log.d("API", "ERROR: " + response.code())
                        //Toast.makeText(this@MainActivity, "Error: ${JSONObject(response.errorBody()?.string()).getString("description")}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Ejemplo>, t: Throwable) {
                    Log.d("API", "Fallo llamada a la API: " + t.message)
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
            val intent = Intent(this, ConciertosActivity::class.java).apply {
                putExtra("idUsuario", 1)
            }
            startActivity(intent)
        }
    }
}