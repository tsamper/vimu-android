package com.tsamper.vimu.modelo

data class Recinto(
    val id: Int = 0,
    val nombre: String = "",
    val direccion: String = "",
    val ciudad: String = "",
    val telefono: String = "",
    val correo: String = "",
    val enlaceMaps: String = ""
)
