package com.example.projectomoviles

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.projectomoviles.Notification.Notification
import com.example.projectomoviles.calendario.Event
import com.example.projectomoviles.calendario.calendario
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class activity_home  : Base_Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var sessionManager: sessionManager;
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        sessionManager = sessionManager(this);

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
            finish();
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
            val intent = Intent(this, Notification::class.java)
            Event.eventsList.clear()
            startActivity(intent)
            finish();
        }

        tvNotifications.setOnClickListener {
            // Cambiar calendario::class.java -> por el de notificación cuando esté hecho.
            val intent = Intent(this, Notification::class.java)
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

    }
    private fun redirectToLoginActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        finish();
    }
}