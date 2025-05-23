package com.tsamper.vimu.modelo

data class Usuario(
    val id: Int = 0,
    val nomUsuario: String = "",
    val contrasenya: String = "",
    val email: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val grupoUsuarios: GrupoUsuarios? = null
)
