package com.tsamper.vimu.modelo

import com.tsamper.vimu.modelo.enums.Privilegios

data class GrupoUsuarios(
    val id: Int = 0,
    val privilegios: Privilegios? = null
)
