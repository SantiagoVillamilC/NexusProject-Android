package com.example.nexusproject_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusproject_android.db.Producto
import com.example.nexusproject_android.db.dbCarrito

class ProductAdapter(private val productList: List<MainActivityCliente.Product>, private val context: Context, private val idUsuario: Int) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.text = product.nombre
        holder.productPrice.text = "$${product.precio}"
        holder.productImage.setImageResource(product.imageUrl)

        holder.btnAddToCart.setOnClickListener {
            agregarAlCarrito(product.idProducto)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    private fun agregarAlCarrito(idProducto: Int) {
        val dbCarrito = dbCarrito(context)
        // Verificamos si el producto ya está en el carrito
        val cantidadActual = dbCarrito.obtenerCantidadProducto(idUsuario, idProducto)

        if (cantidadActual > 0) {
            // Si ya está en el carrito, solo actualizamos la cantidad
            val cantidadNueva = cantidadActual + 1
            val result = dbCarrito.actualizarCantidadProducto(idUsuario, idProducto, cantidadNueva)

            if (result > 0) {
                Toast.makeText(context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al actualizar el carrito", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si no está en el carrito, lo insertamos
            val result = dbCarrito.insertarProductoCarrito(idUsuario, idProducto, 1)

            if (result > 0) {
                Toast.makeText(context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al agregar al carrito", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

