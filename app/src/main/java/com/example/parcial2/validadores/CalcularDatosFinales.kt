package com.example.parcial2.presentacion

import com.google.android.gms.maps.model.LatLng
import android.location.Location
import kotlin.math.ceil

class CalcularDatosFinales(
    private val stock: Double,
    private val bombas: Int
) : Validador {
    private var siguiente: Validador? = null

    override fun setSiguiente(s: Validador) {
        siguiente = s
    }

    override fun verificar(datos: DatosFila): Boolean {
        val litrosPorAuto = 30.0
        val largoAuto = 5.0
        val minutosPorAuto = 3

        val distancia = calcularDistanciaMetros(datos.puntos)
        val autos = ceil(distancia / largoAuto).toInt().coerceAtLeast(1)
        val litrosNecesarios = autos * litrosPorAuto

        datos.cantidadBombas = bombas

        datos.stockDisponible = stock
        datos.litrosNecesarios = litrosNecesarios.toInt() // <- Guardás el valor aquí
        datos.alcanza = litrosNecesarios <= stock
        datos.tiempoEstimado = (autos * minutosPorAuto) / bombas

        return siguiente?.verificar(datos) ?: true
    }


    private fun calcularDistanciaMetros(puntos: List<LatLng>): Double {
        var metros = 0.0
        for (i in 1 until puntos.size) {
            val res = FloatArray(1)
            Location.distanceBetween(
                puntos[i - 1].latitude, puntos[i - 1].longitude,
                puntos[i].latitude, puntos[i].longitude,
                res
            )
            metros += res[0]
        }
        return metros
    }
}
