package com.example.nexusproject_android

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusproject_android.CartItem
import com.example.nexusproject_android.R

class CartActivity : AppCompatActivity() {
    private lateinit var cartItems: MutableList<CartItem>
    private lateinit var emptyCartMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Obtener cartItems del Intent
        cartItems = intent.getParcelableArrayListExtra<CartItem>("cartItems") ?: mutableListOf()

        emptyCartMessage = findViewById(R.id.emptyCartMessage)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(cartItems)

        // Verificar si el carrito está vacío y mostrar el mensaje
        updateCartMessage()
    }

    private fun updateCartMessage() {
        if (cartItems.isEmpty()) {
            emptyCartMessage.visibility = View.VISIBLE
        } else {
            emptyCartMessage.visibility = View.GONE
        }
    }

    private inner class CartAdapter(private val cartItems: MutableList<CartItem>) :
        RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

        inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val productName: TextView = view.findViewById(R.id.productName)
            val productQuantity: TextView = view.findViewById(R.id.productQuantity)
            val increaseButton: Button = view.findViewById(R.id.increaseButton)
            val decreaseButton: Button = view.findViewById(R.id.decreaseButton)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_item, parent, false)
            return CartViewHolder(view)
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            val cartItem = cartItems[position]
            holder.productName.text = cartItem.product.name
            holder.productQuantity.text = cartItem.quantity.toString()

            holder.increaseButton.setOnClickListener {
                cartItem.quantity++
                notifyItemChanged(position)
                updateCartMessage()  // Actualizar el mensaje si el carrito cambia
            }

            holder.decreaseButton.setOnClickListener {
                if (cartItem.quantity > 1) {
                    cartItem.quantity--
                    notifyItemChanged(position)
                } else {
                    cartItems.removeAt(position)
                    notifyItemRemoved(position)
                }
                updateCartMessage()  // Actualizar el mensaje si el carrito cambia
            }
        }

        override fun getItemCount() = cartItems.size
    }
}
