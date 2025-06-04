package com.tsamper.vimu.modelo

import com.tsamper.vimu.modelo.enums.OpcionesOpinion
import java.time.LocalDate

data class Opinion(
    val id: Int = 0,
    val usuario: Usuario? = null,
    val grupo: Grupo? = null,
    val concierto: Concierto? = null,
    val comentario: String = "",
    val fecha: String = "",
    val recomendado: OpcionesOpinion? = null
)
