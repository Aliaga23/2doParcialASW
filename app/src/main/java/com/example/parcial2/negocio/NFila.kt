package com.example.parcial2.negocio

import android.content.Context
import com.example.parcial2.datos.DFila
import com.example.parcial2.datos.Fila

class NFila(context: Context) {
    private val dFila = DFila(context)

    fun insertar(fila: Fila): Boolean {
        return dFila.insertar(fila)
    }

    fun listar(): List<Fila> {
        return dFila.listar()
    }
}
