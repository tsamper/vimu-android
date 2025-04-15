package com.tsamper.vimu.conexion


import com.tsamper.vimu.modelo.Ejemplo
import com.tsamper.vimu.modelo.LoginRequest
import com.tsamper.vimu.modelo.Usuario
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    //@Headers("Accept: application/json")
    @GET("demo")
    fun ejemplo(): Call<Ejemplo>
    @POST("usuarios/buscar")
    fun login(@Body login: LoginRequest): Call<Usuario>
}
