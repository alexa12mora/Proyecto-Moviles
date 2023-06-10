package com.example.projectomoviles

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class activity_user_d : Base_Activity() {
    private lateinit var sessionManager: sessionManager
    private var userId: Int = 0
    private lateinit var dbHelper: MyDatabaseHelper
    //Declaracion de variables
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var repitepassword: EditText
    private lateinit var name: EditText
    private lateinit var edad: EditText
    private lateinit var button: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_d)
        dbHelper = MyDatabaseHelper(this)
        username =  findViewById(R.id.username_input);
        password =  findViewById(R.id.pass);
        repitepassword=  findViewById(R.id.pass_ver);
        name =  findViewById(R.id.name);
        edad =  findViewById(R.id.edad);
        button =  findViewById(R.id.buttonUpdate);
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        sessionManager = sessionManager(this);
        userId = sessionManager.getUserId();
        loadData();
        button.setOnClickListener {
            val enteredUsername = username.text.toString().trim();
            val enteredPassword = password.text.toString().trim();
            val enteredRepeatPassword = repitepassword.text.toString().trim();
            val enteredName =  name.text.toString().trim();
            val enterededad = edad.text.toString().trim();
            resetInputFields();
            if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty() && enteredRepeatPassword.isNotEmpty() && enteredName.isNotEmpty() && enterededad.isNotEmpty()) {
                if (enteredPassword == enteredRepeatPassword) {
                    dbHelper.updateData(enteredUsername, enteredPassword,enteredName,enterededad.toInt());
                    showToast("Datos actualizado correctamente");
                    val intent = Intent(this, activity_home::class.java);
                    startActivity(intent);
                    finish();
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
                findViewById<RelativeLayout>(R.id.relativeloyoutNombre).setBackgroundResource(R.drawable.red_border);
                findViewById<RelativeLayout>(R.id.relativeloyoutEdad).setBackgroundResource(R.drawable.red_border);
            }
        }
    }
    @SuppressLint("Range")
    private fun loadData() {
        val db = dbHelper.readableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(userId.toString())
        val cursor = db.query("users", null, selection, selectionArgs, null, null, null)
        if (cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndex("username"));
            val password = cursor.getString(cursor.getColumnIndex("password"));
            val name = cursor.getString(cursor.getColumnIndex("name"));
            val edad = cursor.getString(cursor.getColumnIndex("edad"));
            val usernameEditText = findViewById<EditText>(R.id.username_input);
            val passwordEditText = findViewById<EditText>(R.id.pass);
            val passwordVerEditText = findViewById<EditText>(R.id.pass_ver);
            val nameVertextEdit = findViewById<EditText>(R.id.name);
            val edadVerTextEdit = findViewById<EditText>(R.id.edad);
            usernameEditText.setText(username);
            passwordEditText.setText(password);
            passwordVerEditText.setText(password);
            nameVertextEdit.setText(name);
            edadVerTextEdit.setText(edad);
        }
        cursor.close();
        db.close();
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