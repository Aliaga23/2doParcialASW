package com.example.parcial2.presentacion

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.parcial2.R
import com.example.parcial2.datos.*
import com.example.parcial2.negocio.*
import com.example.parcial2.validadores.*
import com.example.parcial2.observador.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.text.SimpleDateFormat
import java.util.*

class PFila : Fragment() {

    private lateinit var spinnerTipo: Spinner
    private lateinit var spinnerEstacion: Spinner
    private lateinit var txtResultado: TextView
    private lateinit var txtStock: TextView
    private lateinit var btnConfirmar: Button

    private lateinit var nTipo: NTipoCombustible
    private lateinit var nEstacion: NEstacion
    private lateinit var nStock: NStockCombustible
    private lateinit var nFila: NFila

    private lateinit var listaTipos: List<DTipoCombustible>
    private lateinit var listaEstaciones: List<Estacion>
    private var estacionesFiltradas: List<Estacion> = listOf()

    private var puntosDibujo = mutableListOf<LatLng>()
    private var googleMap: GoogleMap? = null
    private var marcadorEstacion: Marker? = null
    private var polyline: Polyline? = null

    private val gestorEventos = GestorEventosFila()
    private lateinit var observadorResumen: ObservadorResumenTextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_fila, container, false)

        spinnerTipo = v.findViewById(R.id.spinnerTipoFila)
        spinnerEstacion = v.findViewById(R.id.spinnerEstacionFila)
        txtResultado = v.findViewById(R.id.txtResumen)
        txtStock = v.findViewById(R.id.txtStockDisponible)
        btnConfirmar = v.findViewById(R.id.btnConfirmarFila)

        nTipo = NTipoCombustible(requireContext())
        nEstacion = NEstacion(requireContext())
        nStock = NStockCombustible(requireContext())
        nFila = NFila(requireContext())

        listaTipos = nTipo.listar()
        listaEstaciones = nEstacion.listar()

        val placeholderTipo = DTipoCombustible(0, "Seleccionar Tipo")
        val tiposConPlaceholder = listOf(placeholderTipo) + listaTipos
        spinnerTipo.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, tiposConPlaceholder.map { it.nombre })

        val placeholderEstacion = Estacion(0, "Seleccionar Estación", "", 0.0, 0.0)
        spinnerEstacion.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf(placeholderEstacion.nombre))

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFila) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            it.uiSettings.isZoomControlsEnabled = true
            it.setOnMapClickListener { punto ->
                puntosDibujo.add(punto)
                it.addMarker(MarkerOptions().position(punto))
                polyline?.remove()
                polyline = it.addPolyline(PolylineOptions().addAll(puntosDibujo).color(0xFF0000FF.toInt()))
            }
        }

        spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val tipo = tiposConPlaceholder[pos]
                if (tipo.id == 0) {
                    spinnerEstacion.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Seleccionar Estación"))
                    estacionesFiltradas = listOf()
                    return
                }

                val stockList = nStock.listar()
                estacionesFiltradas = listaEstaciones.filter { est ->
                    stockList.any { it.tipoId == tipo.id && it.estacionId == est.id }
                }

                val estacionesConPlaceholder = listOf(placeholderEstacion) + estacionesFiltradas
                spinnerEstacion.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, estacionesConPlaceholder.map { it.nombre })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerEstacion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                actualizarStock()
                mostrarEstacionEnMapa()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Registrar observador
        observadorResumen = ObservadorResumenTextView(txtResultado)
        gestorEventos.suscribir(observadorResumen)

        btnConfirmar.setOnClickListener {
            val tipo = tiposConPlaceholder.getOrNull(spinnerTipo.selectedItemPosition)?.takeIf { it.id != 0 }
            val estacion = (listOf(placeholderEstacion) + estacionesFiltradas).getOrNull(spinnerEstacion.selectedItemPosition)?.takeIf { it.id != 0 }

            val stock = nStock.obtenerLitrosDisponibles(estacion?.id ?: 0, tipo?.id ?: 0) ?: 0.0
            val bombas = NBomba(requireContext()).obtenerCantidad(estacion?.id ?: 0, tipo?.id ?: 0).coerceAtLeast(1)

            val datos = DatosFila(tipo, estacion, puntosDibujo)

            // Cadena de responsabilidad
            val validarTipo = ValidarTipo()
            val validarEstacion = ValidarEstacion()
            val validarPuntos = ValidarPuntos()
            val calcularFinal = CalcularDatosFinales(stock, bombas)

            validarTipo.setSiguiente(validarEstacion)
            validarEstacion.setSiguiente(validarPuntos)
            validarPuntos.setSiguiente(calcularFinal)

            val esValido = validarTipo.verificar(datos)
            if (!esValido) {
                Toast.makeText(context, "Error: ${datos.error}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fila = Fila(
                estacionId = datos.estacion!!.id,
                tipoId = datos.tipo!!.id,
                tiempoEstimado = datos.tiempoEstimado,
                alcanzaCombustible = datos.alcanza,
                fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )
            nFila.insertar(fila)
            gestorEventos.notificar(fila)
        }

        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gestorEventos.desuscribir(observadorResumen)
    }

    private fun actualizarStock() {
        val tipo = listaTipos.getOrNull(spinnerTipo.selectedItemPosition - 1)
        val estacion = estacionesFiltradas.getOrNull(spinnerEstacion.selectedItemPosition - 1)
        val stock = nStock.obtenerLitrosDisponibles(estacion?.id ?: 0, tipo?.id ?: 0)
        txtStock.text = "Stock disponible: ${stock ?: "N/D"} litros"
    }

    private fun mostrarEstacionEnMapa() {
        val estacion = estacionesFiltradas.getOrNull(spinnerEstacion.selectedItemPosition - 1) ?: return
        val ubicacion = LatLng(estacion.latitud, estacion.longitud)
        googleMap?.apply {
            marcadorEstacion?.remove()
            marcadorEstacion = addMarker(
                MarkerOptions()
                    .position(ubicacion)
                    .title(estacion.nombre)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            )
            animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 17f))
        }
    }
}
