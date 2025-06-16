package com.example.parcial2.negocio

import android.content.Context
import com.example.parcial2.datos.DStockCombustible
import com.example.parcial2.datos.StockCombustible

class NStockCombustible(context: Context) {
    private val dStock = DStockCombustible(context)

    fun listar(): List<StockCombustible> = dStock.listar()

    fun insertar(stock: StockCombustible): Boolean = dStock.insertar(stock)

    fun actualizar(stock: StockCombustible): Boolean = dStock.actualizar(stock)

    fun eliminar(id: Int): Boolean = dStock.eliminar(id)
    fun obtenerLitrosDisponibles(estacionId: Int, tipoId: Int): Double? {
        return dStock.obtenerLitrosDisponibles(estacionId, tipoId)
    }

}
