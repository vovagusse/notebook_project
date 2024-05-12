package com.example.notebook_project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.notebook_project.activities.EditorActivity
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

        val drawerLayout: DrawerLayout = vb.drawerLayout

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_github_link,
                R.id.nav_settings
            ),
            drawerLayout
        )
        val navHostFragment: NavHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_content_main)
                    as NavHostFragment

        val navController: NavController = navHostFragment.navController
        val navView: NavigationView = vb.navView
        navController.navigate(R.id.nav_home)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//      old implementation with dumb fkin snackbar
//        vb.appBarMain.fabCreateNewNotebook.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val createButton = vb.appBarMain.fabCreateNewNotebook
        createButton.setOnClickListener {
            Intent(this, EditorActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_appbar_options, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId)
    {
        R.id.item_action_settings -> {
            // LAUNCH SETTINGS ACTIVITY
//            vb.drawerLayout.openDrawer(GravityCompat.START)
            /*return*/ true
        }
        else -> super.onOptionsItemSelected(item)
    }

}