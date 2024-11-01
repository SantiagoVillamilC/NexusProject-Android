    package com.example.nexusproject_android

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.TextView
    import android.widget.Button
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.GridLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.nexusproject_android.db.dbProductos
    import android.widget.Toast
    import androidx.appcompat.app.AlertDialog

    class MainActivityCliente : AppCompatActivity() {

        data class Product(
            val idProducto: Int,
            val nombre: String,
            val descripcion: String?,
            val precio: Double,
            val cantidadStock: Int,
            val imageUrl: Int
        )

        private val cartItems = mutableListOf<CartItem>()
        private lateinit var productList: List<Product>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main_cliente)

            val nombreUsuario = intent.getStringExtra("usuario_nombre")
            findViewById<TextView>(R.id.txtwelcomeText).text = "Hola, $nombreUsuario"

            // Cargar productos iniciales en la base de datos
            cargarProductosIniciales()

            findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btnViewCart).setOnClickListener {
                Log.d("MainActivityCliente", "Cart Items: $cartItems")
                val intent = Intent(this, CartActivity::class.java)
                intent.putParcelableArrayListExtra("cartItems", ArrayList(cartItems))
                startActivity(intent)
            }

            findViewById<Button>(R.id.btnLogout).setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Cerrar Sesión")
                    .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                    .setPositiveButton("Sí") { _, _ ->
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }

        private fun cargarProductosIniciales() {
            val dbProductos = dbProductos(this)
            dbProductos.cargarProductosIniciales()

            val productos = dbProductos.obtenerProductos().map { producto ->
                Product(
                    idProducto = producto.idProducto,
                    nombre = producto.nombre,
                    descripcion = producto.descripcion,
                    precio = producto.precio,
                    cantidadStock = producto.cantidadStock,
                    imageUrl = producto.imageUrl
                )
            }
            productList = productos

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = GridLayoutManager(this, 2)
            recyclerView.adapter = ProductAdapter(productList)
        }


    }
