package com.example.nexusproject_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusproject_android.db.dbCarrito

class CartActivity : AppCompatActivity() {
    private lateinit var cartItems: MutableList<CartItem>
    private lateinit var emptyCartMessage: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var dbCarrito: dbCarrito
    private lateinit var totalPriceTextView: TextView
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Inicializar dbCarrito
        dbCarrito = dbCarrito(this)

        // Obtener el id del usuario
        val userId = 1  // Reemplazar con el id del usuario activo

        // Obtener los productos del carrito
        cartItems = obtenerProductosDelCarrito(userId)

        // Asignar vistas
        emptyCartMessage = findViewById(R.id.emptyCartMessage)
        recyclerView = findViewById(R.id.recyclerViewCart)
        totalPriceTextView = findViewById(R.id.totalPrice)
        confirmButton = findViewById(R.id.confirmButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(this, cartItems, userId) // Pasa el contexto y el usuario
        recyclerView.adapter = cartAdapter

        updateCartMessage()
        updateTotalPrice()

        confirmButton.setOnClickListener {
            // Funcionalidad de confirmar compra (por implementar más adelante)
            Toast.makeText(this, "Compra confirmada (funcionalidad pendiente)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerProductosDelCarrito(userId: Int): MutableList<CartItem> {
        val carritoItemsFromDb = dbCarrito.obtenerProductosDelCarrito(userId)
        val cartItems = mutableListOf<CartItem>()
        for (item in carritoItemsFromDb) {
            val cartItem = CartItem(
                product = Product(
                    idProducto = item.idProducto,
                    name = item.nombreProducto,
                    price = item.precioProducto,
                    imageUrl = item.imagenProducto
                ),
                quantity = item.cantidad
            )
            cartItems.add(cartItem)
        }
        return cartItems
    }

    private fun updateCartMessage() {
        if (cartItems.isEmpty()) {
            emptyCartMessage.visibility = View.VISIBLE
        } else {
            emptyCartMessage.visibility = View.GONE
        }
    }

    public fun updateTotalPrice() {
        val totalPrice = cartItems.sumByDouble { it.product.price * it.quantity }
        totalPriceTextView.text = "Total: \$${"%.2f".format(totalPrice)}"
    }

}


