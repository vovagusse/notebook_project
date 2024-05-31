package com.example.notebook_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.notebook_project.databinding.ActivityMainBinding
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.NotebookRepository_Impl
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository_Impl
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    //fields
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var vb: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var notebookViewModel: NotebookViewModel
    // ON CREATEEEEE AYOOO
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        notebookViewModel = ViewModelProvider(this,
            NotebookViewModelFactory(
                NotebookRepository_Impl.getInstance(this),
                UserPreferencesRepository_Impl(
                    this.dataStore,
                ),
                this.application
            )
        )[NotebookViewModel::class.java]



        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)


        //Fragment mother (Mother of all Fragments)
        val navHostFragment: NavHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_content_main)
                    as NavHostFragment


        // DrawerLayout setup.
        drawerLayout = vb.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_fragment,
                R.id.nav_github_link,
                R.id.nav_settings
            ),
            drawerLayout
        )
        //locks the drawerLayout object in case the fragment is not HomeFragment. Pretty cool, huh?
        navHostFragment.navController.addOnDestinationChangedListener(listener = { _, destination, _ ->
            if (destination.id== R.id.nav_home_fragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        })


        //setting shit up
        setSupportActionBar(vb.appBarMain.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = ""
//        supportActionBar?.subtitle = ""
//        supportActionBar?.setDisplayShowTitleEnabled(false)


        //navController thing
        navController = navHostFragment.navController
        val navView: NavigationView = vb.navView
        navController.navigate(R.id.nav_home_fragment)


        // action bar setup
        setupActionBarWithNavController(navController, appBarConfiguration)


        // DrawerLayout navigation setup
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.nav_github_link).setOnMenuItemClickListener {
            val my_repo = resources.getText(R.string.my_github_repo).toString()
            Intent(Intent.ACTION_VIEW).apply {
                this.setData(Uri.parse(my_repo))
                startActivity(this)
            }
            drawerLayout.closeDrawers()
            true
        }
        navView.menu.findItem(R.id.nav_settings).setOnMenuItemClickListener {
            navController.navigate(R.id.action_nav_home_to_settingsFragment)
            drawerLayout.closeDrawers()
            //            Toast
//                .makeText(
//                    this,
//                    "Currently not implemented, sowwy :3",
//                    Toast.LENGTH_LONG)
//                .show()
            true
        }
        navView.menu.findItem(R.id.nav_openFile).setOnMenuItemClickListener {
//            TODO("bitch i ait did the thing with opening file :(")
            Intent(Intent.ACTION_GET_CONTENT).apply {
                this.setType("text/md/txt")
                this.addCategory(Intent.CATEGORY_OPENABLE)
                val a = 1
                try{
                    startActivityForResult(
                        Intent.createChooser(
                            this,
                            resources.getText(R.string.select_file_message)
                        ),
                        a
                    )
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(this@MainActivity,
                        resources.getText(R.string.err_no_file_manager),
                        Toast.LENGTH_LONG).show()
                }
            }
            drawerLayout.closeDrawers()
            true
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
}