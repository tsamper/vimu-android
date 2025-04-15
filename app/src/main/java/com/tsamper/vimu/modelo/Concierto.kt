package com.tsamper.vimu.modelo

import java.time.LocalDate
import java.time.LocalTime

data class Concierto (
    val id: Int = 0,
    val nombre: String = "",
    val imagen: String = "",
    val recinto: Recinto? = null,
    val fecha: String = "",
    val hora: String = "",
    val cantidadEntradasVip: Int = 0,
    val cantidadEntradasVipVendidas: Int = 0,
    val precioEntradasVip: Int = 0,
    val cantidadEntradas: Int = 0,
    val cantidadEntradasVendidas: Int = 0,
    val precioEntradas: Int = 0,
    val promotor: Usuario,
    val grupo: Grupo
    )
