package com.example.parcial2.presentacion

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.parcial2.R
import com.example.parcial2.negocio.NEstacion

class PEstacion : Fragment() {

    private lateinit var nEstacion: NEstacion
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val vista = inflater.inflate(R.layout.fragment_estacion, container, false)

        nEstacion = NEstacion(requireContext())
        listView = vista.findViewById(R.id.listViewEstaciones)

        cargarDatos()

        return vista
    }

    private fun cargarDatos() {
        val estaciones = nEstacion.listar()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            estaciones.map {
                "${it.nombre}\n${it.direccion} (Lat: ${it.latitud}, Lng: ${it.longitud})"
            }
        )
        listView.adapter = adapter
    }
}
