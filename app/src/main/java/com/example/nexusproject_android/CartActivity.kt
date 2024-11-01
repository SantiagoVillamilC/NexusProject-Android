package com.example.nexusproject_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class CartActivity : AppCompatActivity() {
    private lateinit var cartItems: MutableList<CartItem>
    private lateinit var emptyCartMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartItems = intent.getParcelableArrayListExtra<CartItem>("cartItems") ?: mutableListOf()

        emptyCartMessage = findViewById(R.id.emptyCartMessage)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(cartItems)

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
            val productImage: ImageView = view.findViewById(R.id.productImage)  // ImageView para mostrar la imagen
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

            // Cargar la imagen desde el nombre del recurso drawable
            val imageResId = holder.itemView.context.resources.getIdentifier(
                cartItem.product.imageUrl, "drawable", holder.itemView.context.packageName
            )
            holder.productImage.setImageResource(imageResId)

            holder.increaseButton.setOnClickListener {
                cartItem.quantity++
                notifyItemChanged(position)
                updateCartMessage()
                Toast.makeText(holder.itemView.context, "Cantidad de ${cartItem.product.name} aumentada", Toast.LENGTH_SHORT).show()
            }

            holder.decreaseButton.setOnClickListener {
                if (cartItem.quantity > 1) {
                    cartItem.quantity--
                    notifyItemChanged(position)
                } else {
                    cartItems.removeAt(position)
                    notifyItemRemoved(position)
                }
                updateCartMessage()
                Toast.makeText(holder.itemView.context, "Cantidad de ${cartItem.product.name} disminuida", Toast.LENGTH_SHORT).show()
            }
        }

        override fun getItemCount() = cartItems.size
    }
}
