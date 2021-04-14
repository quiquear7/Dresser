package com.uc3m.dresser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.uc3m.dresser.databinding.ActivityMainBinding
import com.uc3m.dresser.ui.auth.AuthFragment


enum class ProviderType{
    GOOGLE
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Dresser)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private val INTERVALO = 2000 //2 segundos para salir

    private var tiempoPrimerClick: Long = 0

    override fun onBackPressed() {
        val currentFragment = findNavController(R.id.nav_host_fragment).currentDestination
        if (currentFragment != null) {
            if(currentFragment.id == R.id.authFragment || currentFragment.id == R.id.navigation_home ){
                if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
                    finish()
                }else {
                    Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show()
                }
                tiempoPrimerClick = System.currentTimeMillis()

            }else{
                val count = supportFragmentManager.backStackEntryCount
                if (count == 0) {
                    super.onBackPressed()
                } else {
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_ajustes, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.bLogout -> {
                auth = FirebaseAuth.getInstance()
                auth.signOut()
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }






}

