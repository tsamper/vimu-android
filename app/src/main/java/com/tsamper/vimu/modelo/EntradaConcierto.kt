package com.tsamper.vimu.modelo

import java.time.LocalDateTime

data class EntradaConcierto(
    val id: Int = 0,
    val precio: Double = 0.0,
    val usuario: Usuario? = null,
    val tipo: String = "",
    val fechaCompra: String? = "",
    val concierto: Concierto? = null
)
