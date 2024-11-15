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
import com.example.nexusproject_android.db.dbCarrito
import com.example.nexusproject_android.CartActivity

class CartAdapter(private val context: Context, private val cartList: MutableList<CartItem>, private val idUsuario: Int) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productQuantity: TextView = view.findViewById(R.id.productQuantity)
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productName: TextView = view.findViewById(R.id.productName)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val increaseButton: Button = view.findViewById(R.id.increaseButton)
        val decreaseButton: Button = view.findViewById(R.id.decreaseButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.productName.text = cartItem.product.name
        holder.productQuantity.text = cartItem.quantity.toString()
        holder.productImage.setImageResource(cartItem.product.imageUrl.toInt())
        holder.productPrice.text = "$${"%.2f".format(cartItem.product.price)}"


        holder.increaseButton.setOnClickListener {
            actualizarCantidad(cartItem.product.idProducto, cartItem.quantity + 1, position, holder)
        }

        holder.decreaseButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                actualizarCantidad(cartItem.product.idProducto, cartItem.quantity - 1, position, holder)
            } else {
                // Eliminar producto de la base de datos y del RecyclerView
                val dbCarrito = dbCarrito(context)
                val result = dbCarrito.eliminarProductoDelCarrito(cartItem.product.idProducto)
                if (result > 0) {
                    cartList.removeAt(position) // Eliminar del RecyclerView
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartList.size) // Actualizar el rango
                    (context as CartActivity).updateTotalPrice() // Actualizar el precio total
                } else {
                    Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private fun actualizarCantidad(idProducto: Int, nuevaCantidad: Int, position: Int, holder: CartViewHolder) {
        val dbCarrito = dbCarrito(context)

        if (nuevaCantidad > 0) {
            // Actualizar la cantidad en la base de datos
            val result = dbCarrito.actualizarCantidadProducto(idUsuario, idProducto, nuevaCantidad)
            if (result > 0) {
                // Actualiza la cantidad en la lista y notifica el cambio
                cartList[position].quantity = nuevaCantidad
                holder.productQuantity.text = nuevaCantidad.toString()
                notifyItemChanged(position)
                (context as CartActivity).updateTotalPrice() // Actualiza el total
            } else {
                Toast.makeText(context, "Error al actualizar la cantidad", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si la cantidad es 0, eliminar el producto
            val carritoItemId = cartList[position].product.idProducto
            dbCarrito.eliminarProductoDelCarrito(carritoItemId)
            cartList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartList.size)
            (context as CartActivity).updateTotalPrice() // Actualiza el total
        }
    }

}
