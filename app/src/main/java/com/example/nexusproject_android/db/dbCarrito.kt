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
    fun insertarProductoCarrito(idUsuario: Int, idProducto: Int, cantidad: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("id_usuario", idUsuario)
            put("id_producto", idProducto)
            put("cantidad", cantidad)
        }
        val result = db.insert(TABLE_CARRITO, null, values)
        db.close()
        return if (result == -1L) 0 else 1  // Retorna 0 si hay error, 1 si fue exitoso
    }


    // Método para obtener los productos del carrito de un usuario "viejo"
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

    // Obtener la cantidad de un producto en el carrito
    fun obtenerCantidadProducto(idUsuario: Int, idProducto: Int): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT cantidad FROM $TABLE_CARRITO WHERE id_usuario = ? AND id_producto = ?", arrayOf(idUsuario.toString(), idProducto.toString()))
        var cantidad = 0
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
        }
        cursor.close()
        db.close()
        return cantidad
    }

    // Actualizar la cantidad de un producto en el carrito
    fun actualizarCantidadProducto(idUsuario: Int, idProducto: Int, cantidad: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("cantidad", cantidad)
        }
        val result = db.update(TABLE_CARRITO, values, "id_usuario = ? AND id_producto = ?", arrayOf(idUsuario.toString(), idProducto.toString()))
        db.close()
        return result
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