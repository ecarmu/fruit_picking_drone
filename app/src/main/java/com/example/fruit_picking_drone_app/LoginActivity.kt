package com.example.fruit_picking_drone_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            // giriş kontrolü burada yapılır
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // login activity'yi kapat
        }
    }
}