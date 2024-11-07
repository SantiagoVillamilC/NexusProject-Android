package com.example.nexusproject_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onRemoveClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productQuantity: TextView = itemView.findViewById(R.id.productQuantity)
        val removeButton: ImageView = itemView.findViewById(R.id.decreaseButton)

        fun bind(cartItem: CartItem, onRemoveClick: (CartItem) -> Unit) {
            productName.text = cartItem.product.name
            productQuantity.text = "Cantidad: ${cartItem.quantity}"
            removeButton.setOnClickListener { onRemoveClick(cartItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem, onRemoveClick)
    }

    override fun getItemCount(): Int = cartItems.size
}
