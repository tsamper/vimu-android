package com.tsamper.vimu.actividades


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tsamper.vimu.R
import com.tsamper.vimu.conexion.RetrofitClient
import com.tsamper.vimu.modelo.GrupoUsuarios
import com.tsamper.vimu.modelo.Usuario
import com.tsamper.vimu.modelo.enums.Privilegios
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val registro: Button = findViewById(R.id.registrarBtn)
        val nomUsuario: EditText = findViewById(R.id.usuario)
        val password: EditText = findViewById(R.id.password)
        val confirmPassword: EditText = findViewById(R.id.confirmPassword)
        val email: EditText = findViewById(R.id.email)
        val nombre: EditText = findViewById(R.id.nombre)
        val apellidos: EditText = findViewById(R.id.apellidos)
        val grupoUsuarios: Spinner = findViewById(R.id.spinner)
        val opciones = listOf("usuario", "promotor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        grupoUsuarios.adapter = adapter
        var correcto: Boolean = false;
        confirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (password.text.toString() != confirmPassword.text.toString()) {
                    confirmPassword.error = "Las contraseñas no coinciden"
                } else {
                    confirmPassword.error = null
                    correcto = true;
                }
            }
        }
        password.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (password.text.toString() != confirmPassword.text.toString()) {
                    confirmPassword.error = "Las contraseñas no coinciden"
                } else {
                    confirmPassword.error = null
                    correcto = true;
                }
            }
        }


        registro.setOnClickListener {
            if (nomUsuario.text.toString().isEmpty() || password.text.toString().isEmpty() || email.text.toString().isEmpty() ||
                nombre.text.toString().isEmpty() || apellidos.text.toString().isEmpty()){
                correcto = false
            }
            if(correcto){
                val grupo = GrupoUsuarios()
                if (grupoUsuarios.selectedItem.toString().equals("usuario")){
                    grupo.tipo = Privilegios.USER
                }else if (grupoUsuarios.selectedItem.toString().equals("promotor")){
                    grupo.tipo = Privilegios.EVENTO
                }
                val usuario = Usuario(0,nomUsuario.text.toString(), password.text.toString(), email.text.toString(), nombre.text.toString() , apellidos.text.toString(), grupo)
                val apiService = RetrofitClient.getApiService()
                apiService.registrarUsuario(usuario).enqueue(object : Callback<Usuario> {
                    @SuppressLint("NotifyDataSetChanged")
                    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        Log.d("API", "Registrar usuario")
                        if (response.isSuccessful) {
                            response.body()?.let {
                                Toast.makeText(this@RegistroActivity, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.d("API", "ERROR: " + response.code())
                        }
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Log.d("API", "Fallo llamada a la API: " + t.message)
                    }
                })
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Hay campos incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}