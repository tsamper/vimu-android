package com.tsamper.vimu.actividades

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tsamper.vimu.R
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.LoginRequest
import com.tsamper.vimu.modelo.Usuario
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
        var idUser: Int = 0
        val sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val usuario: EditText = findViewById(R.id.usuario)
        val password: EditText = findViewById(R.id.password)
        val registro: TextView = findViewById(R.id.registro)
        val login: Button = findViewById(R.id.iniciarBtn)
        val checkbox: CheckBox = findViewById(R.id.checkBox)
        val recordar = sharedPreferences.getBoolean("recordar", false)
        if (recordar) {
            idUser = sharedPreferences.getInt("user_id", 0)
            val tipoUSer = sharedPreferences.getString("user_tipo", "")
            checkbox.isChecked = true
            val intent = Intent(this@MainActivity, ConciertosActivity::class.java).apply {
                putExtra("idUsuario", idUser)
                putExtra("tipoUsuario", tipoUSer)
            }
            startActivity(intent)
            finish()
        }

        registro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {
            var loginRequest = LoginRequest(usuario.text.toString(), password.text.toString())
            val apiService = RetrofitClient.getApiService()
            apiService.login(loginRequest).enqueue(object : Callback<Usuario> {
                @SuppressLint("NotifyDataSetChanged")
                @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    Log.d("API", "Accediendo llamada a la API")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Log.d("API", "Cuerpo de la respuesta: ${response.body()}")
                            Log.d("API", "" + it)
                            idUser = it.id
                            if (checkbox.isChecked) {
                                sharedPreferences.edit()
                                    .putInt("user_id", it.id)
                                    .putString("user_tipo", it.grupoUsuarios.toString())
                                    .putBoolean("recordar", true)
                                    .apply()
                            } else {
                            sharedPreferences.edit().clear().apply()
                        }
                            val intent = Intent(this@MainActivity, ConciertosActivity::class.java).apply {
                                putExtra("idUsuario", idUser)
                                putExtra("tipoUsuario", it.grupoUsuarios?.tipo.toString() )
                            }
                            startActivity(intent)
                        }
                    } else {
                        Log.d("API", "ERROR: " + response.code())

                        Toast.makeText(this@MainActivity, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Log.d("API", "Fallo llamada a la API: " + t.message)
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })


        }
    }
}