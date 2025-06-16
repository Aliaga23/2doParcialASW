package com.example.parcial2.presentacion

class ValidarTipo : Validador {
    private var siguiente: Validador? = null

    override fun setSiguiente(s: Validador) {
        siguiente = s
    }

    override fun verificar(datos: DatosFila): Boolean {
        return if (datos.tipo == null) {
            datos.error = "Selecciona un tipo de combustible"
            false
        } else {
            siguiente?.verificar(datos) ?: true
        }
    }
}
