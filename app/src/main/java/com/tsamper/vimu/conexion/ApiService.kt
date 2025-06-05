package com.tsamper.vimu.conexion


import com.tsamper.vimu.modelo.Cancion
import com.tsamper.vimu.modelo.Concierto
import com.tsamper.vimu.modelo.Ejemplo
import com.tsamper.vimu.modelo.EntradaConcierto
import com.tsamper.vimu.modelo.Grupo
import com.tsamper.vimu.modelo.LoginRequest
import com.tsamper.vimu.modelo.Opinion
import com.tsamper.vimu.modelo.Recinto
import com.tsamper.vimu.modelo.Usuario
import com.tsamper.vimu.modelo.enums.OpcionesOpinion
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("demo")
    fun ejemplo(): Call<Ejemplo>
    //Llamadas Usuarios
    @GET("usuarios/{id}")
    fun obtenerUsuarioPorId(@Path("id") id: Int): Call<Usuario>
    @GET("usuarios/{id}")
    suspend fun obtenerUsuarioPorIdSuspend(@Path("id") id: Int): Usuario
    @POST("usuarios/buscar")
    fun login(@Body login: LoginRequest): Call<Usuario>
    @POST("usuarios")
    fun registrarUsuario(@Body usuario: Usuario): Call<Usuario>
    //Llamadas Canciones
    @GET("canciones")
    fun obtenerCancionesPorGrupo(@Query("grupoId") grupoId: Int): Call<List<Cancion>>
    //Llamadas Conciertos
    @GET("conciertos")
    fun obtenerConciertos(): Call<ArrayList<Concierto>>
    @GET("conciertos/promotor/")
    fun obtenerConciertosPorPromotor(@Query("promotor") userId: Int): Call<List<Concierto>>
    @GET("conciertos/filtro")
    fun buscarConciertos(@Query("filtro") filtro: String, @Query("campo") campo: String): Call<ArrayList<Concierto>>
    @GET("conciertos/{id}")
    fun obtenerConciertoPorId(@Path("id") id: Int): Call<Concierto>
    @POST("entradas")
    fun comprarEntradas(@Query("conciertoId") conciertoId: Int, @Query("user") userId: Int, @Query("cantidadSeleccionadaNormal") cantidadSeleccionadaNormal: Int, @Query("cantidadSeleccionadaVip") cantidadSeleccionadaVip: Int ): Call<Void>
    @GET("conciertos/guardados")
    fun obtenerConciertosGuardados(@Query("user") userId: Int): Call<ArrayList<Concierto>>
    @POST("conciertosGuardados")
    fun guardarConciertoGuardado(@Query("idConcierto") idConcierto: Int, @Query("user") userId: Int): Call<Int>
    @DELETE("guardados/{idConcierto}")
    fun eliminarGuardado(@Path("idConcierto") idConcierto: Int, @Query("user") userId: Int): Call<Void>
    @GET("conciertos/old")
    fun obtenerConciertosAnteriores(@Query("user") userId: Int): Call<Map<String, List<Concierto>>>
    @DELETE("conciertos/{id}")
    fun eliminarConcierto(@Path("id") id: Int): Call<Void>
    @POST("conciertos")
    suspend fun registrarConcierto(@Body concierto: Concierto, @Query("user") userId: Int): Response<Void>
    //Llamadas Entradas
    @GET("entradas")
    fun obtenerEntradasUsuario(@Query("user") userId: Int): Call<Map<String, List<EntradaConcierto>>>
    //Llamadas Grupos
    @GET("grupos/{id}")
    fun obtenerGrupoPorId(@Path("id") id: Int): Call<Grupo>
    @GET("grupos")
    fun obtenerGrupos(): Call<List<Grupo>>
    @GET("grupos/nombre/{nombre}")
    suspend fun obtenerGrupoPorNombre(@Path("nombre") id: String): Grupo
    //LLamadas Opiniones
    @POST("opiniones/{idConcierto}/{idUsuario}")
    fun registrarComentario(@Path("idConcierto") idConcierto: Int, @Path("idUsuario") idUsuario: Int, @Query("recomendado") recomendado: OpcionesOpinion, @Body comentario: String): Call<Void>
    @GET("opiniones/{idConcierto}")
    fun comprobarComentarioPorUsuarioYConcierto(@Path("idConcierto") idConcierto: Int, @Query("user") userId: Int): Call<Boolean>
    @GET("opiniones")
    fun obtenerOpinionesPorGrupo(@Query("grupoId") grupoId: Int): Call<List<Opinion>>
    //Lamadas Recintos
    @GET("recintos")
    fun obtenerRecintos(): Call<List<Recinto>>
    @GET("recintos/{id}")
    fun obtenerRecintoPorId(@Path("id") id: Int): Call<Recinto>
    @GET("recintos/nombre/{nombre}")
    suspend fun obtenerRecintoPorNombre(@Path("nombre") nombre: String): Recinto
}
