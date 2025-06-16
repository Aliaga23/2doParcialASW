package com.example.parcial2.negocio

import android.content.Context
import com.example.parcial2.datos.DEstacion
import com.example.parcial2.datos.Estacion

class NEstacion(context: Context) {
    private val dEstacion = DEstacion(context)

    fun listar(): List<Estacion> = dEstacion.listar()
}
