package com.example.parcial2.observador

import android.widget.TextView
import com.example.parcial2.datos.Fila

class ObservadorResumenTextView(private val txtView: TextView) : ObservadorFila {
    override fun actualizar(fila: Fila) {
        txtView.text = """
            ¿Alcanza?: ${if (fila.alcanzaCombustible) "Sí ✅" else "No ❌"}
            Tiempo estimado: ${fila.tiempoEstimado} min
            Fecha: ${fila.fecha}
        """.trimIndent()
    }
}
