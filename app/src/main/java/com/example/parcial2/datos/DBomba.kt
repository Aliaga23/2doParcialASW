package com.example.parcial2.datos

import android.content.Context

data class DBomba(
    val id: Int = 0,
    val estacionId: Int = 0,
    val tipoId: Int = 0,
    val cantidad: Int = 0
) {
    private lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context
    }

    fun listar(): List<DBomba> {
        val lista = mutableListOf<DBomba>()
        val dbHelper = BaseDatosHelper(context)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Bomba ORDER BY id", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val estacionId = cursor.getInt(cursor.getColumnIndexOrThrow("estacionId"))
                val tipoId = cursor.getInt(cursor.getColumnIndexOrThrow("tipoId"))
                val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
                lista.add(DBomba(id, estacionId, tipoId, cantidad))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

    fun obtenerCantidad(estacionId: Int, tipoId: Int): Int {
        val dbHelper = BaseDatosHelper(context)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT cantidad FROM Bomba WHERE estacionId = ? AND tipoId = ?",
            arrayOf(estacionId.toString(), tipoId.toString())
        )
        var total = 0
        if (cursor.moveToFirst()) {
            do {
                total += cursor.getInt(0)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return total
    }
}
