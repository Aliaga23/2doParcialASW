package com.example.parcial2.negocio

import android.content.Context
import com.example.parcial2.datos.DBomba

class NBomba(context: Context) {
    private val dBomba = DBomba(context)

    fun listar(): List<DBomba> {
        return dBomba.listar()
    }

    fun obtenerCantidad(estacionId: Int, tipoId: Int): Int {
        return dBomba.obtenerCantidad(estacionId, tipoId)
    }
}
