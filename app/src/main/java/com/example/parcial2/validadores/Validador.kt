package com.example.parcial2.presentacion

interface Validador {
    fun setSiguiente(siguiente: Validador)
    fun verificar(datos: DatosFila): Boolean
}
