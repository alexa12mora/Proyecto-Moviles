package com.example.projectomoviles

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.projectomoviles.Notification.Notification
import com.example.projectomoviles.calendario.Event
import com.example.projectomoviles.calendario.calendario
import com.google.android.material.bottomnavigation.BottomNavigationView

open class Base_Activity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_perfil -> {
                val intent = Intent(this, activity_user_d::class.java)
                Event.eventsList.clear()
                startActivity(intent)
                return true
            }
            R.id.nav_gallery -> {
                val intent = Intent(this, activity_home::class.java)
                Event.eventsList.clear()
                startActivity(intent)
                return true
            }
            R.id.nav_calendar -> {
                val intent = Intent(this, calendario::class.java)
                Event.eventsList.clear()
                startActivity(intent)
                return true
            }
            R.id.nav_notification -> {
                val intent = Intent(this, Notification::class.java)
                Event.eventsList.clear()
                startActivity(intent)
                return true
            }

        }
        return false
    }
}