package com.example.projectomoviles

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import es.dmoral.toasty.Toasty
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class MainActivity : AppCompatActivity() {
    //Base de datos
    private lateinit var dbHelper: MyDatabaseHelper
    //Declaracion de variables
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var button: Button
    private lateinit var notienecuenta: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = MyDatabaseHelper(this)
        username = findViewById(R.id.username_input)
        password = findViewById(R.id.pass)
        button = findViewById(R.id.buttonLogin)
        notienecuenta = findViewById(R.id.no_tiene_cuenta)
        val sessionManager = sessionManager(this);

        button.setOnClickListener {
            val inputUsername = username.text.toString().trim()
            val inputPassword = password.text.toString().trim()
            resetInputFields()
            if (validateInputs(inputUsername, inputPassword)) {
                if (dbHelper.chechUser(inputUsername)) {
                    if (dbHelper.chechUserPassword(inputUsername, inputPassword)) {
                        Toasty.success(this, "Inicio de sesion exitoso!", Toast.LENGTH_LONG, true).show()
                        sessionManager.startSession(dbHelper.getIdUser(inputUsername));
                        val intent = Intent(this, activity_home::class.java);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Aplicar animación de desvanecimiento
                        finish();
                    } else {
                        Toasty.error(this, "Credenciales inválidas!", Toast.LENGTH_LONG, true).show()
                        findViewById<RelativeLayout>(R.id.relativeLoyoutContrasenia).setBackgroundResource(R.drawable.red_border)
                        findViewById<RelativeLayout>(R.id.relativeLoyoutCorreo).setBackgroundResource(R.drawable.red_border)
                    }
                } else {
                    Toasty.warning(this, "Usuario no encontrado!", Toast.LENGTH_LONG, true).show()
                }
            } else {
                Toasty.warning(this, "Por favor, complete todos los campos!", Toast.LENGTH_LONG, true).show()
            }
        }
        notienecuenta.setOnClickListener {
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Aplicar animación de desvanecimiento
            finish();
        }
    }
    private fun validateInputs(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty();
    }

    private fun resetInputFields() {
        findViewById<RelativeLayout>(R.id.relativeLoyoutContrasenia).setBackgroundResource(R.drawable.blue_border_rounded_cornwe);
        findViewById<RelativeLayout>(R.id.relativeLoyoutCorreo).setBackgroundResource(R.drawable.blue_border_rounded_cornwe);
    }
}