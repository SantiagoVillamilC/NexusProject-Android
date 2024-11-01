package com.example.nexusproject_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nexusproject_android.LoginActivity
import com.example.nexusproject_android.RegisterActivity
import com.example.nexusproject_android.db.DbHelper
import com.example.nexusproject_android.db.dbProductos

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val dbHelper = DbHelper(this)
            val db = dbHelper.writableDatabase
            if (db.isOpen) {
                Toast.makeText(this, "Base de datos funcionando correctamente", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error en la base de datos", Toast.LENGTH_LONG).show()
            }
            db.close()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al crear la base de datos: ${e.message}", Toast.LENGTH_LONG).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnLoginMain = findViewById<Button>(R.id.btnLoginMain)
        btnLoginMain.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnRegisterMain = findViewById<Button>(R.id.btnRegisterMain)
        btnRegisterMain.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
