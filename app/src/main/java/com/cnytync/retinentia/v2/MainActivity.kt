package com.cnytync.retinentia.v2

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.cnytync.retinentia.v2.util.ZipUtility
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import java.io.*
import com.cnytync.retinentia.v2.R


class MainActivity : AppCompatActivity() {
    val zipUtility: ZipUtility = ZipUtility()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val token = intent.getStringExtra("token")
        val userId = intent.getIntExtra("userId",-1)

        setContentView(R.layout.activity_main)

        var drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        findViewById<ImageButton>(R.id.open_menu_button).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        var bundle: Bundle = Bundle()
        bundle.putString("token", token)
        bundle.putInt("userId", userId)

        var navigationView: NavigationView = findViewById(R.id.navigation_view)
        var navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment_feed)
        navController.setGraph(R.navigation.main, bundle)
        NavigationUI.setupWithNavController(navigationView, navController)

        val titleView: TextView = findViewById<TextView>(R.id.main_title)
        navController.addOnDestinationChangedListener { controller,destination, arguments ->
            titleView.setText(destination.label)
        }

        var feedItem = navigationView.menu.findItem(R.id.menu_feed)
        feedItem.setChecked(true)
        feedItem.setOnMenuItemClickListener {
            navController.navigate(R.id.nav_frag_feed,bundle)
            drawerLayout.closeDrawer(GravityCompat.START)
            feedItem.setChecked(true)
            true
        }

//        var booksItem = navigationView.menu.findItem(R.id.menu_books)
//        booksItem.setOnMenuItemClickListener {
//            navController.navigate(R.id.nav_frag_books,bundle)
//            drawerLayout.closeDrawer(GravityCompat.START)
//            booksItem.setChecked(true)
//            true
//        }

        var categoriesItem = navigationView.menu.findItem(R.id.menu_categories)
        categoriesItem.setOnMenuItemClickListener {
            navController.navigate(R.id.nav_frag_categories,bundle)
            drawerLayout.closeDrawer(GravityCompat.START)
            categoriesItem.setChecked(true)
            true
        }

        var memoryItem = navigationView.menu.findItem(R.id.menu_memory)
        memoryItem.setOnMenuItemClickListener {
            navController.navigate(R.id.nav_frag_memory,bundle)
            drawerLayout.closeDrawer(GravityCompat.START)
            memoryItem.setChecked(true)
            true
        }

        var graphItem = navigationView.menu.findItem(R.id.menu_graph)
        graphItem.setOnMenuItemClickListener {
            navController.navigate(R.id.nav_frag_graph,bundle)
            drawerLayout.closeDrawer(GravityCompat.START)
            graphItem.setChecked(true)
            true
        }

        var searchItem = navigationView.menu.findItem(R.id.menu_search)
        searchItem.setOnMenuItemClickListener {
            navController.navigate(R.id.nav_frag_search,bundle)
            drawerLayout.closeDrawer(GravityCompat.START)
            searchItem.setChecked(true)
            true
        }

        var studyItem = navigationView.menu.findItem(R.id.menu_study)
        studyItem.setOnMenuItemClickListener {
            navController.navigate(R.id.nav_frag_study,bundle)
            drawerLayout.closeDrawer(GravityCompat.START)
            studyItem.setChecked(true)
            true
        }

    }

}

private fun <T> Call<T>?.enqueue(any: Any) {

}
