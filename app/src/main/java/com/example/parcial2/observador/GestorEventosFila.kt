package com.example.parcial2.observador

import android.util.Log
import com.example.parcial2.datos.Fila

class GestorEventosFila {
    private val observadores = mutableListOf<ObservadorFila>()

    fun suscribir(obs: ObservadorFila) {
        observadores.add(obs)
        Log.d("GestorEventosFila", "Suscrito: $obs")
    }

    fun desuscribir(obs: ObservadorFila) {
        observadores.remove(obs)
        Log.d("GestorEventosFila", "Desuscrito: $obs")
    }

    fun notificar(fila: Fila) {
        observadores.forEach { it.actualizar(fila) }
    }
}
