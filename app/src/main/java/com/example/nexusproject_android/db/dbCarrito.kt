package com.example.nexusproject_android.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

data class CarritoItem(
    val id: Int = 0,
    val idUsuario: Int,
    val idProducto: Int,
    val cantidad: Int
)

class dbCarrito(context: Context) : DbHelper(context) {

    // Método para insertar un producto en el carrito
    fun insertarProductoCarrito(idUsuario: Int, idProducto: Int, cantidad: Int): Long {
        var id: Long = 0
        try {
            val db: SQLiteDatabase = this.writableDatabase

            val values = ContentValues().apply {
                put("id_usuario", idUsuario)
                put("id_producto", idProducto)
                put("cantidad", cantidad)
            }

            id = db.insert(TABLE_CARRITO, null, values)
            db.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return id
    }

    // Método para obtener los productos del carrito de un usuario
    fun obtenerProductosDelCarrito(idUsuario: Int): List<CarritoItem> {
        val items = mutableListOf<CarritoItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CARRITO WHERE id_usuario = ?", arrayOf(idUsuario.toString()))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"))
                val idProducto = cursor.getInt(cursor.getColumnIndexOrThrow("id_producto"))
                val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))

                items.add(CarritoItem(id, idUsuario, idProducto, cantidad))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return items
    }

    // Método para eliminar un producto del carrito
    fun eliminarProductoDelCarrito(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CARRITO, "id = ?", arrayOf(id.toString()))
        db.close()
    }

    // Método para limpiar el carrito de un usuario
    fun vaciarCarrito(idUsuario: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CARRITO, "id_usuario = ?", arrayOf(idUsuario.toString()))
        db.close()
    }
}
