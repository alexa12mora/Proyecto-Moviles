package com.example.projectomoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.projectomoviles.calendario.calendario
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class activity_home : Base_Activity(){
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
    }
    private fun redirectToLoginActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        finish();
    }
}