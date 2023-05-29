package com.example.projectomoviles

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast

class activity_register : AppCompatActivity() {
    private lateinit var dbHelper: MyDatabaseHelper
    //Declaracion de variables
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var repitepassword: EditText
    private lateinit var yatienecuenta: Button
    private lateinit var button: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = MyDatabaseHelper(this)
        username =  findViewById(R.id.username_input);
        password =  findViewById(R.id.pass);
        repitepassword=  findViewById(R.id.pass_ver);
        yatienecuenta=  findViewById(R.id.ya_tiene_cuenta);
        button =  findViewById(R.id.buttonLogin);

        button.setOnClickListener {
            val enteredUsername = username.text.toString().trim()
            val enteredPassword = password.text.toString().trim()
            val enteredRepeatPassword = repitepassword.text.toString().trim()
            resetInputFields();
            if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty() && enteredRepeatPassword.isNotEmpty()) {
                if (enteredPassword == enteredRepeatPassword) {
                    if (dbHelper.chechUser(enteredUsername)) {
                        showToast("Usuario ya existe, inicie sesión");
                    } else {
                        dbHelper.insertData(enteredUsername, enteredPassword);
                        showToast("Usuario creado correctamente");
                        val intent = Intent(this, MainActivity::class.java);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    showToast("Las contraseñas no coinciden");
                    findViewById<RelativeLayout>(R.id.relativeloyoutContra).setBackgroundResource(R.drawable.red_border);
                    findViewById<RelativeLayout>(R.id.relativeloyoutRepiContra).setBackgroundResource(R.drawable.red_border);
                }
            } else {
                showToast("Por favor, complete todos los campos ya que son requeridos");
                findViewById<RelativeLayout>(R.id.relativeloyoutCorreo).setBackgroundResource(R.drawable.red_border);
                findViewById<RelativeLayout>(R.id.relativeloyoutContra).setBackgroundResource(R.drawable.red_border);
                findViewById<RelativeLayout>(R.id.relativeloyoutRepiContra).setBackgroundResource(R.drawable.red_border);
            }
        }

        yatienecuenta.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun resetInputFields() {
        findViewById<RelativeLayout>(R.id.relativeloyoutCorreo).setBackgroundResource(R.drawable.blue_border_rounded_cornwe);
        findViewById<RelativeLayout>(R.id.relativeloyoutContra).setBackgroundResource(R.drawable.blue_border_rounded_cornwe);
        findViewById<RelativeLayout>(R.id.relativeloyoutRepiContra).setBackgroundResource(R.drawable.blue_border_rounded_cornwe);
    }
}