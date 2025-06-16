package com.example.parcial2.presentacion

class ValidarPuntos : Validador {
    private var siguiente: Validador? = null

    override fun setSiguiente(s: Validador) {
        siguiente = s
    }

    override fun verificar(datos: DatosFila): Boolean {
        return if (datos.puntos.size < 2) {
            datos.error = "Dibuja la fila en el mapa"
            false
        } else {
            siguiente?.verificar(datos) ?: true
        }
    }
}
