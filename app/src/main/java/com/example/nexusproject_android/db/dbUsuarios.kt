package com.example.nexusproject_android.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.nexusproject_android.db.DbHelper
import com.example.nexusproject_android.db.DbHelper.Companion.TABLE_USUARIOS

data class Usuario(
    val id: Int = 0,
    val nombreUsuario: String,
    val contrasena: String,
    val correoElectronico: String,
    val preferenciasGeolocalizacion: String? = null,
    val historialUbicaciones: String? = null
)

class dbUsuarios(context: Context) : DbHelper(context) {

    private val context: Context = context

    // Método para insertar un usuario en la base de datos
    fun insertarUsuario(nombreUsuario: String, contrasena: String, correoElectronico: String): Long {
        var id: Long = 0
        try {
            val dbHelper = DbHelper(context)
            val db: SQLiteDatabase = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("nombre_usuario", nombreUsuario)
                put("contrasena", contrasena)
                put("correo_electronico", correoElectronico)
            }

            id = db.insert(TABLE_USUARIOS, null, values)
            db.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return id
    }

    // Método para obtener todos los usuarios de la base de datos
    fun mostrarUsuarios(): ArrayList<Usuario> {
        val listaUsuarios = ArrayList<Usuario>()
        val dbHelper = DbHelper(context)
        val db: SQLiteDatabase = dbHelper.writableDatabase
        var cursorUsuarios: Cursor? = null

        try {
            cursorUsuarios = db.rawQuery("SELECT * FROM $TABLE_USUARIOS ORDER BY nombre_usuario ASC", null)
            if (cursorUsuarios.moveToFirst()) {
                do {
                    val usuario = Usuario(
                        id = cursorUsuarios.getInt(0),
                        nombreUsuario = cursorUsuarios.getString(1),
                        contrasena = cursorUsuarios.getString(2),
                        correoElectronico = cursorUsuarios.getString(3)
                    )
                    listaUsuarios.add(usuario)
                } while (cursorUsuarios.moveToNext())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            cursorUsuarios?.close()
            db.close()
        }
        return listaUsuarios
    }

    // Método para obtener un usuario específico por su id
    fun verUsuario(id: Int): Usuario? {
        val dbHelper = DbHelper(context)
        val db: SQLiteDatabase = dbHelper.writableDatabase
        var usuario: Usuario? = null
        var cursorUsuario: Cursor? = null

        try {
            cursorUsuario = db.rawQuery("SELECT * FROM $TABLE_USUARIOS WHERE id = $id LIMIT 1", null)
            if (cursorUsuario.moveToFirst()) {
                usuario = Usuario(
                    id = cursorUsuario.getInt(0),
                    nombreUsuario = cursorUsuario.getString(1),
                    contrasena = cursorUsuario.getString(2),
                    correoElectronico = cursorUsuario.getString(3)
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            cursorUsuario?.close()
            db.close()
        }
        return usuario
    }

    // Método para actualizar un usuario en la base de datos
    fun editarUsuario(id: Int, nombreUsuario: String, contrasena: String, correoElectronico: String): Boolean {
        var correcto = false
        val dbHelper = DbHelper(context)
        val db: SQLiteDatabase = dbHelper.writableDatabase

        try {
            val values = ContentValues().apply {
                put("nombre_usuario", nombreUsuario)
                put("contrasena", contrasena)
                put("correo_electronico", correoElectronico)
            }
            val result = db.update(TABLE_USUARIOS, values, "id=?", arrayOf(id.toString()))
            correcto = result > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            correcto = false
        } finally {
            db.close()
        }
        return correcto
    }

    // Método para eliminar un usuario de la base de datos
    fun eliminarUsuario(id: Int): Boolean {
        var correcto = false
        val dbHelper = DbHelper(context)
        val db: SQLiteDatabase = dbHelper.writableDatabase

        try {
            val result = db.delete(TABLE_USUARIOS, "id=?", arrayOf(id.toString()))
            correcto = result > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            correcto = false
        } finally {
            db.close()
        }
        return correcto
    }
}