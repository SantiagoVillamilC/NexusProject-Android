package com.example.nexusproject_android.db

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

open class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2 //nueva version
        private const val DATABASE_NAME = "nexus.db"

        // Nombre de las tablas
        const val TABLE_USUARIOS = "Usuarios"
        const val TABLE_PRODUCTOS = "Productos"
        const val TABLE_CARRITO = "Carrito"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla Usuarios
        val createUsuariosTable = """
            CREATE TABLE $TABLE_USUARIOS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre_usuario TEXT NOT NULL,
                contrasena TEXT NOT NULL,
                correo_electronico TEXT NOT NULL,
                preferencias_geolocalizacion TEXT,
                historial_ubicaciones TEXT
            )
        """.trimIndent()
        db.execSQL(createUsuariosTable)

        // Crear tabla Productos
        val createProductosTable = """
            CREATE TABLE $TABLE_PRODUCTOS (
                id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT,
                precio REAL NOT NULL,
                cantidad_stock INTEGER NOT NULL,
                image_url TEXT
            )
        """.trimIndent()
        db.execSQL(createProductosTable)

        // Crear tabla Carrito
        val createCarritoTable = """
            CREATE TABLE $TABLE_CARRITO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario INTEGER,
                id_producto INTEGER,
                nombre_producto TEXT NOT NULL, 
                imagenProducto INTEGER NOT NULL,
                precio REAL NOT NULL,
                cantidad INTEGER NOT NULL,
                FOREIGN KEY (id_usuario) REFERENCES $TABLE_USUARIOS(id)
            )
        """.trimIndent()
        db.execSQL(createCarritoTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si la versión es menor que la nueva, eliminamos las tablas existentes
        if (oldVersion < newVersion) {
            // Eliminar tablas si existen
            try {
                db.execSQL("DROP TABLE IF EXISTS $TABLE_CARRITO")
                db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
                db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTOS")
            } catch (e: Exception) {
                // En caso de algún error, lo registramos
                e.printStackTrace()
            }

            // Llamar a onCreate para crear las tablas nuevamente
            onCreate(db)
        }
    }
}