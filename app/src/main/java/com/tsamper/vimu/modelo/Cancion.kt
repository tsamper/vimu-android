package com.tsamper.vimu.modelo

data class Cancion(
    val id: Int = 0,
    val titulo: String = "",
    val grupo: Grupo? = null,
    val enlaceYoutube: String = ""
)
