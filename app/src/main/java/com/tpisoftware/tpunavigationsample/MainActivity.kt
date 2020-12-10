package com.tpisoftware.tpunavigationsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tpisoftware.tpunavigationsample.extension.setupWithNavController

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navigationGraphIdList = listOf(R.navigation.nav_fox, R.navigation.nav_psyduck, R.navigation.nav_maple)

        val controller = bottomNavigationView.setupWithNavController(
                navGraphIds = navigationGraphIdList,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent
        )

        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    // 如果按 action bar 的 back, pass 給目前的 navigation controller
    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}