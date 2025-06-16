package com.example.parcial2.datos

import android.content.Context

data class DTipoCombustible(
    val id: Int = 0,
    val nombre: String = ""
) {
    private lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context
    }

    fun listar(): List<DTipoCombustible> {
        val lista = mutableListOf<DTipoCombustible>()
        val dbHelper = BaseDatosHelper(context)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM TipoCombustible ORDER BY id", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                lista.add(DTipoCombustible(id, nombre))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }
}
