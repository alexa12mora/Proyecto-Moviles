package com.example.projectomoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class activity_user_d : Base_Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var sessionManager: sessionManager
        var userId: Int
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_d)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        sessionManager = sessionManager(this)
        userId = sessionManager.getUserId()
    }
}