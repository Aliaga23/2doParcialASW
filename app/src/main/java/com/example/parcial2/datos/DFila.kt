package com.example.parcial2.datos

import android.content.Context

data class Fila(
    val id: Int = 0,
    val estacionId: Int,
    val tipoId: Int,
    val tiempoEstimado: Int,
    val alcanzaCombustible: Boolean,
    val fecha: String
)

class DFila(context: Context) {
    private val db = BaseDatosHelper(context).readableDatabase
    private val dbWritable = BaseDatosHelper(context).writableDatabase

    fun insertar(fila: Fila): Boolean {
        val sql = """
            INSERT INTO Fila (estacionId, tipoId, tiempoEstimado, alcanzaCombustible, fecha)
            VALUES (?, ?, ?, ?, datetime('now'))
        """.trimIndent()
        val args = arrayOf(
            fila.estacionId.toString(),
            fila.tipoId.toString(),
            fila.tiempoEstimado.toString(),
            if (fila.alcanzaCombustible) "1" else "0"
        )
        return try {
            dbWritable.execSQL(sql, args)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun listar(): List<Fila> {
        val lista = mutableListOf<Fila>()
        val cursor = db.rawQuery("SELECT * FROM Fila ORDER BY id DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val estacionId = cursor.getInt(cursor.getColumnIndexOrThrow("estacionId"))
                val tipoId = cursor.getInt(cursor.getColumnIndexOrThrow("tipoId"))
                val tiempo = cursor.getInt(cursor.getColumnIndexOrThrow("tiempoEstimado"))
                val alcanza = cursor.getInt(cursor.getColumnIndexOrThrow("alcanzaCombustible")) == 1
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                lista.add(Fila(id, estacionId, tipoId, tiempo, alcanza, fecha))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}
