package com.example.nexusproject_android.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.nexusproject_android.R

data class Producto(
    val idProducto: Int = 0,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val cantidadStock: Int,
    val imageUrl: Int
)

class dbProductos(context: Context) : DbHelper(context) {

    private val context: Context = context

    fun cargarProductosIniciales() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_PRODUCTOS", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        db.close()

        // Solo inserta los productos si la tabla esta vacia
        if (count == 0) {
            val productos = listOf(
                Producto(nombre = "Mochila de Viaje", descripcion = "Mochila de 40L", precio = 1200.0, cantidadStock = 10, imageUrl = R.drawable.maleta),
                Producto(nombre = "Botella Térmica", descripcion = "Botella de acero inoxidable de 750 ml", precio = 500.0, cantidadStock = 20, imageUrl = R.drawable.botella),
                Producto(nombre = "Linterna LED", descripcion = "Linterna con batería recargable", precio = 300.0, cantidadStock = 15, imageUrl = R.drawable.linterna),
                Producto(nombre = "Saco de Dormir", descripcion = "Saco de dormir para temperaturas extremas", precio = 800.0, cantidadStock = 8, imageUrl = R.drawable.saco),
                Producto(nombre = "Binoculares", descripcion = "Binoculares con zoom de 10x", precio = 1000.0, cantidadStock = 5, imageUrl = R.drawable.binoculares),
                Producto(nombre = "Kit de Primeros Auxilios", descripcion = "Kit compacto para emergencias", precio = 200.0, cantidadStock = 25, imageUrl = R.drawable.kit),
                Producto(nombre = "Brújula de Mano", descripcion = "Brújula analógica resistente al agua", precio = 150.0, cantidadStock = 30, imageUrl = R.drawable.brujula),
                Producto(nombre = "Cargador Solar", descripcion = "Cargador solar portátil con salida USB", precio = 600.0, cantidadStock = 12, imageUrl = R.drawable.cargador),
                Producto(nombre = "Tienda de Campaña", descripcion = "Tienda para 4 personas, resistente al agua", precio = 3500.0, cantidadStock = 4, imageUrl = R.drawable.tienda),
                Producto(nombre = "Estufa Portátil", descripcion = "Estufa compacta de gas para camping", precio = 400.0, cantidadStock = 7, imageUrl = R.drawable.estufa)
            )

            for (producto in productos) {
                insertarProducto(
                    nombre = producto.nombre,
                    descripcion = producto.descripcion,
                    precio = producto.precio,
                    cantidadStock = producto.cantidadStock,
                    imageUrl = producto.imageUrl
                )
            }
        }
    }


    fun insertarProducto(nombre: String, descripcion: String?, precio: Double, cantidadStock: Int, imageUrl: Int): Long {
        var id: Long = 0
        try {
            val dbHelper = DbHelper(context)
            val db: SQLiteDatabase = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("nombre", nombre)
                put("descripcion", descripcion)
                put("precio", precio)
                put("cantidad_stock", cantidadStock)
                put("image_url", imageUrl)
            }

            id = db.insert(TABLE_PRODUCTOS, null, values)
            db.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return id
    }

    fun obtenerProductos(): List<Producto> {
        val productos = mutableListOf<Producto>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTOS", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_producto"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                val cantidadStock = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad_stock"))
                val imageUrl = cursor.getInt(cursor.getColumnIndexOrThrow("image_url"))

                productos.add(Producto(id, nombre, descripcion, precio, cantidadStock, imageUrl))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productos
    }

}
