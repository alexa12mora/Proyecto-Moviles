package com.example.projectomoviles

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import es.dmoral.toasty.Toasty

class activity_register : AppCompatActivity() {
    private lateinit var dbHelper: MyDatabaseHelper
    //Declaracion de variables
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var repitepassword: EditText
    private lateinit var name: EditText
    private lateinit var edad: EditText
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
        name =  findViewById(R.id.name);
        edad =  findViewById(R.id.edad);
        yatienecuenta=  findViewById(R.id.ya_tiene_cuenta);
        button =  findViewById(R.id.buttonLogin);

        button.setOnClickListener {
            val enteredUsername = username.text.toString().trim()
            val enteredPassword = password.text.toString().trim()
            val enteredRepeatPassword = repitepassword.text.toString().trim()
            val enteredName =  name.text.toString().trim();
            val enterededad = edad.text.toString().trim();
            resetInputFields();
            if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty() && enteredRepeatPassword.isNotEmpty() && enteredName.isNotEmpty() && enterededad.isNotEmpty()) {
                if (enteredPassword == enteredRepeatPassword) {
                    if (dbHelper.chechUser(enteredUsername)) {
                        Toasty.warning(this, "Usuario ya existe, inicie sesión!", Toast.LENGTH_LONG, true).show()
                    } else {
                        dbHelper.insertData(enteredUsername, enteredPassword,enteredName,enterededad.toInt());
                        Toasty.info(this, "Usuario creado correctamente!", Toast.LENGTH_LONG, true).show()
                        val intent = Intent(this, MainActivity::class.java);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toasty.error(this, "Las contraseñas no coinciden!", Toast.LENGTH_LONG, true).show()
                    findViewById<RelativeLayout>(R.id.relativeloyoutContra).setBackgroundResource(R.drawable.red_border);
                    findViewById<RelativeLayout>(R.id.relativeloyoutRepiContra).setBackgroundResource(R.drawable.red_border);
                }
            } else {
                showToast("Todos los campos son requeridos");
                findViewById<RelativeLayout>(R.id.relativeloyoutCorreo).setBackgroundResource(R.drawable.red_border);
                findViewById<RelativeLayout>(R.id.relativeloyoutContra).setBackgroundResource(R.drawable.red_border);
                findViewById<RelativeLayout>(R.id.relativeloyoutRepiContra).setBackgroundResource(R.drawable.red_border);
                findViewById<RelativeLayout>(R.id.relativeloyoutNombre).setBackgroundResource(R.drawable.red_border);
                findViewById<RelativeLayout>(R.id.relativeloyoutEdad).setBackgroundResource(R.drawable.red_border);
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
        findViewById<RelativeLayout>(R.id.relativeloyoutNombre).setBackgroundResource(R.drawable.blue_border_rounded_cornwe);
        findViewById<RelativeLayout>(R.id.relativeloyoutEdad).setBackgroundResource(R.drawable.blue_border_rounded_cornwe);
    }
}