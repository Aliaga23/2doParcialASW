package com.example.parcial2.datos

import android.content.Context

data class StockCombustible(
    val id: Int = 0,
    val estacionId: Int,
    val tipoId: Int,
    val litrosDisponibles: Double
)

class DStockCombustible(private val context: Context) {

    private val dbHelper = BaseDatosHelper(context)

    fun listar(): List<StockCombustible> {
        val lista = mutableListOf<StockCombustible>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM StockCombustible ORDER BY id", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val estacionId = cursor.getInt(cursor.getColumnIndexOrThrow("estacionId"))
                val tipoId = cursor.getInt(cursor.getColumnIndexOrThrow("tipoId"))
                val litros = cursor.getDouble(cursor.getColumnIndexOrThrow("litrosDisponibles"))

                lista.add(StockCombustible(id, estacionId, tipoId, litros))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

    fun insertar(stock: StockCombustible): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            db.execSQL(
                "INSERT INTO StockCombustible(estacionId, tipoId, litrosDisponibles) VALUES (?, ?, ?)",
                arrayOf(stock.estacionId, stock.tipoId, stock.litrosDisponibles)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    fun actualizar(stock: StockCombustible): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            db.execSQL(
                "UPDATE StockCombustible SET estacionId = ?, tipoId = ?, litrosDisponibles = ? WHERE id = ?",
                arrayOf(stock.estacionId, stock.tipoId, stock.litrosDisponibles, stock.id)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    fun eliminar(id: Int): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            db.execSQL("DELETE FROM StockCombustible WHERE id = ?", arrayOf(id))
            true
        } catch (e: Exception) {
            false
        }
    }
    fun obtenerLitrosDisponibles(estacionId: Int, tipoId: Int): Double? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT litrosDisponibles FROM StockCombustible WHERE estacionId = ? AND tipoId = ?",
            arrayOf(estacionId.toString(), tipoId.toString())
        )
        val litros = if (cursor.moveToFirst()) cursor.getDouble(0) else null
        cursor.close()
        return litros
    }


}
