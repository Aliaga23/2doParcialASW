package com.example.parcial2.datos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatosHelper(context: Context) : SQLiteOpenHelper(
    context,
    "gasolinerav5.db",  // nombre de la base
    null,
    3
) {
    override fun onCreate(db: SQLiteDatabase) {
        // ----------------- TipoCombustible -----------------
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS TipoCombustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT UNIQUE NOT NULL
            )
        """)

        // ----------------- Estacion -----------------
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Estacion (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                direccion TEXT,
                latitud REAL NOT NULL,
                longitud REAL NOT NULL
            )
        """)

        // ----------------- Bomba -----------------
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Bomba (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                estacionId INTEGER NOT NULL,
                tipoId INTEGER NOT NULL,
                cantidad INTEGER NOT NULL,
                FOREIGN KEY(estacionId) REFERENCES Estacion(id),
                FOREIGN KEY(tipoId) REFERENCES TipoCombustible(id)
            )
        """)

        // ----------------- StockCombustible -----------------
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS StockCombustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                estacionId INTEGER NOT NULL,
                tipoId INTEGER NOT NULL,
                litrosDisponibles REAL NOT NULL,
                fechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(estacionId) REFERENCES Estacion(id),
                FOREIGN KEY(tipoId) REFERENCES TipoCombustible(id)
            )
        """)

        // ----------------- Fila -----------------
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Fila (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                estacionId INTEGER NOT NULL,
                tipoId INTEGER NOT NULL,
                tiempoEstimado INTEGER NOT NULL,
                alcanzaCombustible BOOLEAN NOT NULL,
                fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(estacionId) REFERENCES Estacion(id),
                FOREIGN KEY(tipoId) REFERENCES TipoCombustible(id)
            )
        """)

        // Datos iniciales de ejemplo
        db.execSQL("INSERT INTO TipoCombustible (nombre) VALUES ('Gasolina');")
        db.execSQL("INSERT INTO TipoCombustible (nombre) VALUES ('Diesel');")
        // Estaciones
        db.execSQL("INSERT INTO Estacion (nombre, direccion, latitud, longitud) VALUES ('Berea', 'Calle 1', -17.79976, -63.18077);")
        db.execSQL("INSERT INTO Estacion (nombre, direccion, latitud, longitud) VALUES ('Alemana', 'Calle 2', -17.76784, -63.17067);")
        db.execSQL("INSERT INTO Estacion (nombre, direccion, latitud, longitud) VALUES ('Estación Pirai', 'Av Roque Aguilera', -17.78593, -63.20438);")

        // Bombas
        db.execSQL("INSERT INTO Bomba (estacionId, tipoId, cantidad) VALUES (1, 1, 6);")
        db.execSQL("INSERT INTO Bomba (estacionId, tipoId, cantidad) VALUES (2, 2, 3);")
        db.execSQL("INSERT INTO Bomba (estacionId, tipoId, cantidad) VALUES (3, 1, 2);")
        db.execSQL("INSERT INTO Bomba (estacionId, tipoId, cantidad) VALUES (3, 2, 4);")     }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Fila")
        db.execSQL("DROP TABLE IF EXISTS StockCombustible")
        db.execSQL("DROP TABLE IF EXISTS Bomba")
        db.execSQL("DROP TABLE IF EXISTS Estacion")
        db.execSQL("DROP TABLE IF EXISTS TipoCombustible")
        onCreate(db)
    }
}
