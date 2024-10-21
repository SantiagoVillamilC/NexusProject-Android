package com.example.nexusproject_android.Cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nexusproject_android.R

data class Product(
    val name: String,
    val price: String,
    val imageUrl: Int
)

class MainActivityCliente : AppCompatActivity() {
    private val productList = listOf(
        Product("Producto 1", "$10.00", R.drawable.logotemporal),
        Product("Producto 2", "$15.00", R.drawable.logotemporal),
        Product("Producto 3", "$20.00", R.drawable.logotemporal),
        Product("Producto 4", "$25.00", R.drawable.logotemporal),
        Product("Producto 5", "$30.00", R.drawable.logotemporal),
        Product("Producto 6", "$35.00", R.drawable.logotemporal),
        Product("Producto 7", "$40.00", R.drawable.logotemporal),
        Product("Producto 8", "$45.00", R.drawable.logotemporal),
        Product("Producto 9", "$50.00", R.drawable.logotemporal),
        Product("Producto 10", "$55.00", R.drawable.logotemporal)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cliente)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ProductAdapter(productList)
    }

    private inner class ProductAdapter(private val products: List<Product>) :
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

        inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val productImage: ImageView = view.findViewById(R.id.productImage)
            val productName: TextView = view.findViewById(R.id.productName)
            val productPrice: TextView = view.findViewById(R.id.productPrice)
            val addToCartButton: Button = view.findViewById(R.id.addToCartButton)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_item, parent, false)
            return ProductViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = products[position]
            holder.productName.text = product.name
            holder.productPrice.text = product.price
            holder.productImage.setImageResource(product.imageUrl)

            holder.addToCartButton.setOnClickListener {
                // Lógica para agregar al carrito (opcional)
            }
        }

        override fun getItemCount() = products.size
}}