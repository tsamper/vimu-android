package com.tsamper.vimu.conexion


import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object {

        var baseUrl: String = "http://192.168.4.138:8080"

        private val client by lazy {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val authenticatedRequest = originalRequest.newBuilder()
                        //.header("Authorization", Credentials.basic(VariablesGlobales.usuario, VariablesGlobales.password))
                        .header("Content-Type", "application/json")
                        .build()
                    chain.proceed(authenticatedRequest)
                }
                .build()
        }

        fun getApiService(): ApiService {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client) // Reutiliza el mismo cliente para optimizar
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}