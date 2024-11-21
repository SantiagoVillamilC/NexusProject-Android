package com.example.nexusproject_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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

        dbCarrito = dbCarrito(this)

        // Recibir el ID del usuario
        val userId = intent.getIntExtra("usuario_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show()
            finish() // Salir de la actividad si el ID no es válido
            return
        }

        // Obtener los productos del carrito del usuario
        cartItems = obtenerProductosDelCarrito(userId)

        // Configurar RecyclerView y vistas
        emptyCartMessage = findViewById(R.id.emptyCartMessage)
        recyclerView = findViewById(R.id.recyclerViewCart)
        totalPriceTextView = findViewById(R.id.totalPrice)
        confirmButton = findViewById(R.id.confirmButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(this, cartItems, userId)
        recyclerView.adapter = cartAdapter

        updateCartMessage()
        updateTotalPrice()

        // Configurar el OnClickListener para el botón de confirmación
        confirmButton.setOnClickListener {
            // Crear un Intent para navegar a la PasarelaDePagoActivity
            val intent = Intent(this, PasarelaDePago::class.java)
            // Pasar el total
            intent.putExtra("total_price", totalPriceTextView.text.toString())
            startActivity(intent)  // Iniciar la actividad
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
