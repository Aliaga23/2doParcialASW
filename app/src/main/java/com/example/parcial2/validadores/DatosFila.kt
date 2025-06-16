package com.example.parcial2.presentacion

import com.example.parcial2.datos.DTipoCombustible
import com.example.parcial2.datos.Estacion
import com.google.android.gms.maps.model.LatLng

class DatosFila(
    val tipo: DTipoCombustible?,
    val estacion: Estacion?,
    val puntos: List<LatLng>,
    var stockDisponible: Double = 0.0,
    var cantidadBombas: Int = 0,
    var alcanza: Boolean = false,
    var tiempoEstimado: Int = 0,
    var error: String = "",
    var litrosNecesarios: Int = 0

)
