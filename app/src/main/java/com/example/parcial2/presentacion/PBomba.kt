package com.example.parcial2.presentacion

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.parcial2.R
import com.example.parcial2.negocio.NBomba
import com.example.parcial2.negocio.NEstacion
import com.example.parcial2.negocio.NTipoCombustible

class PBomba : Fragment() {

    private lateinit var nBomba: NBomba
    private lateinit var nEstacion: NEstacion
    private lateinit var nTipo: NTipoCombustible
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val vista = inflater.inflate(R.layout.fragment_bomba, container, false)

        nBomba = NBomba(requireContext())
        nEstacion = NEstacion(requireContext())
        nTipo = NTipoCombustible(requireContext())
        listView = vista.findViewById(R.id.listViewBombas)

        cargarDatos()

        return vista
    }

    private fun cargarDatos() {
        val bombas = nBomba.listar()

        // Mapas para acceder rápido a los nombres por ID
        val mapaEstaciones = nEstacion.listar().associateBy({ it.id }, { it.nombre })
        val mapaTipos = nTipo.listar().associateBy({ it.id }, { it.nombre })

        val detalles = bombas.map {
            val estacionNombre = mapaEstaciones[it.estacionId] ?: "Desconocida"
            val tipoNombre = mapaTipos[it.tipoId] ?: "Desconocido"
            "Estación: $estacionNombre\nTipo: $tipoNombre\nCantidad: ${it.cantidad}"
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, detalles)
        listView.adapter = adapter
    }
}
