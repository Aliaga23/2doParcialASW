package com.example.parcial2.presentacion

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial2.R
import com.example.parcial2.datos.StockCombustible
import com.example.parcial2.negocio.NEstacion
import com.example.parcial2.negocio.NStockCombustible
import com.example.parcial2.negocio.NTipoCombustible

class PStockCombustible : Fragment() {

    private lateinit var nStock: NStockCombustible
    private lateinit var nEstacion: NEstacion
    private lateinit var nTipo: NTipoCombustible

    private lateinit var spinnerEstacion: Spinner
    private lateinit var spinnerTipo: Spinner
    private lateinit var editLitros: EditText
    private lateinit var btnInsertar: Button
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var textSinDatos: TextView

    private var seleccionado: StockCombustible? = null

    private lateinit var listaStock: MutableList<StockCombustible>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_stock_combustible, container, false)

        // Inicializar capa de negocio
        nStock = NStockCombustible(requireContext())
        nEstacion = NEstacion(requireContext())
        nTipo = NTipoCombustible(requireContext())

        // Referencias a vistas del layout del fragmento
        spinnerEstacion = v.findViewById(R.id.spinnerEstacion)
        spinnerTipo = v.findViewById(R.id.spinnerTipo)
        editLitros = v.findViewById(R.id.editLitros)
        btnInsertar = v.findViewById(R.id.btnInsertar)
        recyclerView = v.findViewById(R.id.recyclerViewStock)
        textSinDatos = v.findViewById(R.id.textSinDatos)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Cargar contenido
        cargarSpinners()
        cargarLista()

        // Acciones solo del botón Insertar (los otros están dentro del RecyclerView)
        btnInsertar.setOnClickListener { insertar() }

        return v
    }

    private fun cargarSpinners() {
        val estaciones = nEstacion.listar()
        val tipos = nTipo.listar()

        val layout = android.R.layout.simple_spinner_dropdown_item

        spinnerEstacion.adapter = ArrayAdapter(
            requireContext(),
            layout,
            estaciones.map { it.nombre }
        )

        spinnerTipo.adapter = ArrayAdapter(
            requireContext(),
            layout,
            tipos.map { it.nombre }
        )
    }

    private fun cargarLista() {
        val estaciones = nEstacion.listar().associateBy { it.id }
        val tipos = nTipo.listar().associateBy { it.id }

        listaStock = nStock.listar().toMutableList()

        textSinDatos.visibility = if (listaStock.isEmpty()) View.VISIBLE else View.GONE

        recyclerView.adapter = object : RecyclerView.Adapter<StockViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_stock_combustible, parent, false)
                return StockViewHolder(view)
            }

            override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
                val stock = listaStock[position]
                val estacionNombre = estaciones[stock.estacionId]?.nombre ?: "?"
                val tipoNombre = tipos[stock.tipoId]?.nombre ?: "?"

                holder.textInfo.text =
                    "Estación: $estacionNombre\nTipo: $tipoNombre\nLitros: ${stock.litrosDisponibles}"

                holder.btnEditar.setOnClickListener {
                    seleccionado = stock
                    editLitros.setText(stock.litrosDisponibles.toString())
                    spinnerEstacion.setSelection(nEstacion.listar().indexOfFirst { it.id == stock.estacionId })
                    spinnerTipo.setSelection(nTipo.listar().indexOfFirst { it.id == stock.tipoId })
                }

                holder.btnEliminar.setOnClickListener {
                    nStock.eliminar(stock.id)
                    toast("Eliminado")
                    cargarLista()
                }
            }

            override fun getItemCount(): Int = listaStock.size
        }
    }

    private inner class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textInfo: TextView = view.findViewById(R.id.textInfo)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    private fun insertar() {
        val estacionId = nEstacion.listar()[spinnerEstacion.selectedItemPosition].id
        val tipoId = nTipo.listar()[spinnerTipo.selectedItemPosition].id
        val litros = editLitros.text.toString().toDoubleOrNull()
        if (litros == null) return toast("Litros inválidos")

        if (nStock.insertar(StockCombustible(0, estacionId, tipoId, litros))) {
            toast("Insertado correctamente")
            limpiarCampos()
            cargarLista()
        } else toast("Error al insertar")
    }

    private fun editar() {
        val item = seleccionado ?: return toast("Seleccione un elemento")
        val estacionId = nEstacion.listar()[spinnerEstacion.selectedItemPosition].id
        val tipoId = nTipo.listar()[spinnerTipo.selectedItemPosition].id
        val litros = editLitros.text.toString().toDoubleOrNull()
        if (litros == null) return toast("Litros inválidos")

        if (nStock.actualizar(item.copy(estacionId = estacionId, tipoId = tipoId, litrosDisponibles = litros))) {
            toast("Actualizado correctamente")
            limpiarCampos()
            cargarLista()
        } else toast("Error al actualizar")
    }

    private fun eliminar() {
        val item = seleccionado ?: return toast("Seleccione un elemento")
        if (nStock.eliminar(item.id)) {
            toast("Eliminado correctamente")
            limpiarCampos()
            cargarLista()
        } else toast("Error al eliminar")
    }

    private fun limpiarCampos() {
        editLitros.setText("")
        spinnerEstacion.setSelection(0)
        spinnerTipo.setSelection(0)
        seleccionado = null
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
