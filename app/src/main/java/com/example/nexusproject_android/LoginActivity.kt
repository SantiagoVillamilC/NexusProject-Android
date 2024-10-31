package com.example.nexusproject_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nexusproject_android.db.dbUsuarios

class LoginActivity : AppCompatActivity() {

    private lateinit var txtNombreUsuarioLogin: EditText
    private lateinit var txtContraseñaLogin: EditText
    private lateinit var btnLoginPrincipal: Button
    private lateinit var btnRegisterLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa los elementos de la interfaz
        txtNombreUsuarioLogin = findViewById(R.id.txtNombreUsuarioLogin)
        txtContraseñaLogin = findViewById(R.id.txtContraseñaLogin)
        btnLoginPrincipal = findViewById(R.id.btnLoginPrincipal)
        btnRegisterLogin = findViewById(R.id.btnRegisterLogin)

        // Lógica para el inicio de sesión
        btnLoginPrincipal.setOnClickListener {
            val nombreUsuario = txtNombreUsuarioLogin.text.toString()
            val contrasena = txtContraseñaLogin.text.toString()

            if (nombreUsuario.isNotEmpty() && contrasena.isNotEmpty()) {
                val dbUsuarios = dbUsuarios(this)
                val usuario = dbUsuarios.verUsuarioPorNombreYContrasena(nombreUsuario, contrasena)

                if (usuario != null) {
                    // Ingreso exitoso
                    Toast.makeText(this, "Ingreso exitoso", Toast.LENGTH_LONG).show()
                    // Redirige a MainActivityCliente y pasa el nombre de usuario
                    val intent = Intent(this, MainActivityCliente::class.java).apply {
                        putExtra("usuario_nombre", usuario.nombreUsuario) // Cambia 'nombreUsuario' al campo que corresponde
                    }
                    startActivity(intent)
                    finish() // Finaliza LoginActivity
                } else {
                    // Credenciales incorrectas
                    Toast.makeText(this, "Los datos ingresados no son correctos", Toast.LENGTH_LONG).show()
                }
            } else {
                // Campos vacíos
                Toast.makeText(this, "DEBE LLENAR LOS CAMPOS OBLIGATORIOS", Toast.LENGTH_LONG).show()
            }
        }


        btnRegisterLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
