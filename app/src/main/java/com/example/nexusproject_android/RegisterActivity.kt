package com.example.nexusproject_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nexusproject_android.db.dbUsuarios

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtNombreUsuarioRegister: EditText
    private lateinit var txtContraseñaRegister: EditText
    private lateinit var txtEmailRegister: EditText
    private lateinit var btnRegisterPrincipal: Button
    private lateinit var btnLoginRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar los elmentos de la interfaz
        txtNombreUsuarioRegister = findViewById(R.id.txtNombreUsuarioRegister)
        txtContraseñaRegister = findViewById(R.id.txtContraseñaRegister)
        txtEmailRegister = findViewById(R.id.txtEmailRegister)
        btnRegisterPrincipal = findViewById(R.id.btnRegisterPrincipal)
        btnLoginRegister = findViewById(R.id.btnLoginRegister)

        // Registro de usuario
        btnRegisterPrincipal.setOnClickListener {
            val nombreUsuario = txtNombreUsuarioRegister.text.toString()
            val contrasena = txtContraseñaRegister.text.toString()
            val correoElectronico = txtEmailRegister.text.toString()

            if (nombreUsuario.isNotEmpty() && contrasena.isNotEmpty() && correoElectronico.isNotEmpty()) {
                val dbUsuarios = dbUsuarios(this)
                val id = dbUsuarios.insertarUsuario(nombreUsuario, contrasena, correoElectronico)

                if (id > 0) {
                    Toast.makeText(this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show()
                    // Redirige a LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza RegisterActivity
                } else {
                    Toast.makeText(this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "DEBES LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show()
            }
        }

        // Ir a MainActivity
        btnLoginRegister.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
