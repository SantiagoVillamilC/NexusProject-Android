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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Inicializar dbCarrito
        dbCarrito = dbCarrito(this)

        // Obtener el id del usuario (esto puede venir del login o sesión activa)
        val userId = 1  // Reemplazar con el id del usuario activo

        // Obtener los productos del carrito desde la base de datos
        cartItems = obtenerProductosDelCarrito(userId)

        emptyCartMessage = findViewById(R.id.emptyCartMessage)
        recyclerView = findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartAdapter = CartAdapter(cartItems)
        recyclerView.adapter = cartAdapter

        updateCartMessage()
    }

    private fun obtenerProductosDelCarrito(userId: Int): MutableList<CartItem> {
        // Obtener los productos del carrito desde la base de datos
        val carritoItemsFromDb = dbCarrito.obtenerProductosDelCarrito(userId)

        // Convertir CarritoItem a CartItem
        val cartItems = mutableListOf<CartItem>()
        for (item in carritoItemsFromDb) {
            // Usar la información directamente de CarritoItem
            val cartItem = CartItem(
                product = Product(
                    idProducto = item.idProducto,
                    name = item.nombreProducto,
                    price = item.precioProducto,
                    imageUrl = item.imagenProducto // Usar el recurso de imagen directamente
                ),
                quantity = item.cantidad
            )
            cartItems.add(cartItem)
        }
        return cartItems
    }

/*
    private fun getProductById(productId: Int): Product {
        // Consulta a la base de datos o acceder a una lista de productos
        // Aquí se debe devolver un producto con su respectiva imagen como un recurso entero (Int)
        return Product(productId, "Producto de ejemplo", 0.0, R.drawable.globo_aerostatico)  // Usa el recurso de imagen
    }*/

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
            val productImage: ImageView = view.findViewById(R.id.productImage)
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

            // Usar el recurso de imagen directamente
            holder.productImage.setImageResource(cartItem.product.imageUrl)

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
