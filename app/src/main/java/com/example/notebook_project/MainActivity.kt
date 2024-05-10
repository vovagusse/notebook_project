package com.example.notebook_project

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import com.example.notebook_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var vb: ActivityMainBinding
    //exportSchema
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setSupportActionBar(vb.appBarMain.toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.subtitle = ""
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        val manrope_bold = Typeface.createFromAsset(this.assets, "fonts/manrope_bold.ttf")


        vb.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = vb.drawerLayout


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow
            ),
            drawerLayout
        )

//        val navView: NavigationView = vb.navView
//        val navController : NavController?
//        try{
//            navController = findNavController(R.id.nav_host_fragment_content_main)
//        } catch (e: Exception){
//            Log.e("MainActivity", "onCreate: not found nav controller i guess", )
//        }
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId)
    {
        R.id.item_action_settings -> {
//            vb.drawerLayout.openDrawer(GravityCompat.START)
            /*return*/ true
        }
        else -> super.onOptionsItemSelected(item)
    }
}