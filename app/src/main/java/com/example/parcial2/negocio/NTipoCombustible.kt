package com.example.parcial2.negocio

import android.content.Context
import com.example.parcial2.datos.DTipoCombustible

class NTipoCombustible(context: Context) {
    private val dTipo = DTipoCombustible(context)

    fun listar(): List<DTipoCombustible> {
        return dTipo.listar()
    }
}
