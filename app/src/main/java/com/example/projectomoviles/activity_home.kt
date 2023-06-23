package com.example.projectomoviles

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

import com.example.projectomoviles.calendario.Event
import com.example.projectomoviles.calendario.calendario
import com.google.android.material.appbar.MaterialToolbar
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit


class activity_home : Base_Activity(){
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var sessionManager: sessionManager
    private var userId: Int = 0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var sessionManager: sessionManager;
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        sessionManager = sessionManager(this);
        userId = sessionManager.getUserId();
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar);
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_perfil -> {
                    sessionManager.endSession();
                    redirectToLoginActivity();
                    true;
                }
                else -> false
            }
        }

        val boxUser = findViewById<LinearLayout>(R.id.boxUser)
        val boxCalendar = findViewById<LinearLayout>(R.id.boxCalendar)
        val boxNotification = findViewById<LinearLayout>(R.id.boxNotification)
        val boxMain = findViewById<LinearLayout>(R.id.boxMain)
        val tvUser = findViewById<TextView>(R.id.tvUser)
        val tvCalendar = findViewById<TextView>(R.id.tvCalendar)
        val tvNotifications = findViewById<TextView>(R.id.tvNotifications)
        val tvLogout = findViewById<TextView>(R.id.tvLogout)


        boxUser.setOnClickListener {
            val intent = Intent(this, activity_user_d::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Aplicar animación de desvanecimiento
            finish()
        }

        tvUser.setOnClickListener {
            val intent = Intent(this, activity_user_d::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            finish();
        }

        boxCalendar.setOnClickListener {
            val intent = Intent(this, calendario::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            finish();
        }

        tvCalendar.setOnClickListener {
            val intent = Intent(this, calendario::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            finish();
        }

        boxNotification.setOnClickListener {
            // Cambiar calendario::class.java -> por el de notificación cuando esté hecho.
            val intent = Intent(this, calendario::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Aplicar animación de desvanecimiento
            finish();
        }

        tvNotifications.setOnClickListener {
            // Cambiar calendario::class.java -> por el de notificación cuando esté hecho.
            val intent = Intent(this, calendario::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            finish();
        }

        boxMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            finish();
        }

        tvLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            finish();
        }
        setAlarmsForEvents();
    }
    private fun redirectToLoginActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        finish();
    }
    @SuppressLint("Range")
    private fun setAlarmsForEvents() {
        dbHelper = MyDatabaseHelper(this)
        val welcomeTextView: TextView = findViewById(R.id.nombreusuario)
        val database: SQLiteDatabase = dbHelper.readableDatabase
        val projection = arrayOf("name")
        val selection = "id = ?"
        val selectionArgs = arrayOf(userId.toString())
        val cursor: Cursor = database.query("users", projection, selection, selectionArgs, null, null, null)
        if (cursor.moveToFirst()) {
            val nombreUsuario = cursor.getString(cursor.getColumnIndex("name"))
            welcomeTextView.text = "Bienvenido $nombreUsuario"
        }
        cursor.close()
        database.close()
    }

}