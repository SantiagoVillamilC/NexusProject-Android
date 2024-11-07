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
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import java.util.Locale

class MainActivityCliente : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var txtLocation: TextView
    private lateinit var btnRetryLocation: Button

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

        // Inicializar elementos UI y servicios de ubicación
        txtLocation = findViewById(R.id.txtLocation)
        btnRetryLocation = findViewById(R.id.btnRetryLocation)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        obtenerUbicacion()

        btnRetryLocation.setOnClickListener {
            obtenerUbicacion()
        }

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

    private fun obtenerUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        } else {
            val useFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                    val geocoder = Geocoder(this, Locale.getDefault())

                    if (useFineLocation) {
                        val latitud = location.latitude
                        val longitud = location.longitude
                        val direcciones = geocoder.getFromLocation(latitud, longitud, 1)

                        if (direcciones != null && direcciones.isNotEmpty()) {
                            val ciudad = direcciones[0].locality ?: "Ciudad desconocida"
                            val pais = direcciones[0].countryName ?: "País desconocido"
                            txtLocation.text = "Tu ubicación: $ciudad, $pais (Lat: $latitud, Long: $longitud)"
                        } else {
                            txtLocation.text = "Latitud: $latitud, Longitud: $longitud. Ciudad desconocida"
                        }
                    } else {
                        val direcciones = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (direcciones != null && direcciones.isNotEmpty()) {
                            val pais = direcciones[0].countryName ?: "País desconocido"
                            txtLocation.text = "Tu ubicación aproximada: $pais"
                        } else {
                            txtLocation.text = "Ubicación aproximada: País desconocido"
                        }
                    }
                    btnRetryLocation.visibility = View.GONE
                } else {
                    txtLocation.text = "No se pudo obtener la ubicación"
                    btnRetryLocation.visibility = View.VISIBLE
                }
            }.addOnFailureListener {
                txtLocation.text = "Error al obtener la ubicación"
                txtLocation.visibility = View.GONE
                btnRetryLocation.visibility = View.VISIBLE
                Toast.makeText(this, "Activa tu ubicación y reintenta", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Manejar la respuesta del usuario a la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    obtenerUbicacion()
                } else if (grantResults.getOrNull(1) == PackageManager.PERMISSION_GRANTED) {
                    obtenerUbicacion()
                } else {
                    Toast.makeText(this, "Permiso denegado. Activa la ubicación y reintenta.", Toast.LENGTH_SHORT).show()
                    btnRetryLocation.visibility = View.VISIBLE
                }
            }
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