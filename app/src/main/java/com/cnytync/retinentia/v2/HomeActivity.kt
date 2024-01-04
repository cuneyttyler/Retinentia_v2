package com.cnytync.retinentia.v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.cnytync.retinentia.v2.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        var drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        findViewById<ImageButton>(R.id.open_menu_button).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        var navigationView: NavigationView = findViewById(R.id.navigation_view)
        var navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment_home)
        NavigationUI.setupWithNavController(navigationView, navController)
    }
}