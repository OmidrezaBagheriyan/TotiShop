package com.omidrezabagherian.totishop.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSplashScreen()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        initBottomNavigation()

        setContentView(mainBinding.root)
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }
    }

    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain)
                    as NavHostFragment
        navController = navHostFragment.navController

        mainBinding.bottomNavigationViewMain.setupWithNavController(navController)

        mainBinding.bottomNavigationViewMain.setOnItemSelectedListener { tab ->
            val fragmentMain = navController.currentDestination?.id?.plus(1)
            val fragmentNext = tab.itemId
            if (fragmentMain!=fragmentNext){
                when (tab.itemId) {
                    R.id.houseTab -> {
                        navController.navigate(R.id.houseFragment)
                    }
                    R.id.categoryTab -> {
                        navController.navigate(R.id.categoryFragment)
                    }
                    R.id.bagTab -> {
                        navController.navigate(R.id.bagFragment)
                    }
                    R.id.userTab -> {
                        navController.navigate(R.id.loginFragment)
                    }
                }
            }
            true
        }
    }

}