package com.tsamper.vimu.modelo

data class Usuario(
    val id: Int = 0,
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val nomUsuario: String = "",
    val contrasenya: String = "",
    val grupoUsuarios: GrupoUsuarios? = null
)
