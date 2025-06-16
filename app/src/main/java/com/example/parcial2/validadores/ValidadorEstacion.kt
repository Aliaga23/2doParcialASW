package com.example.parcial2.presentacion

class ValidarEstacion : Validador {
    private var siguiente: Validador? = null

    override fun setSiguiente(s: Validador) {
        siguiente = s
    }

    override fun verificar(datos: DatosFila): Boolean {
        return if (datos.estacion == null) {
            datos.error = "Selecciona una estación"
            false
        } else {
            siguiente?.verificar(datos) ?: true
        }
    }
}
