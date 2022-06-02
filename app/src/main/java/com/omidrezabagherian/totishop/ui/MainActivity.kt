package com.omidrezabagherian.totishop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val navController by lazy {
        findNavController(R.id.fragmentContainerViewMain)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSplashScreen()

        checkConnect()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        initBottomNavigation()

        setContentView(mainBinding.root)
    }

    private fun checkConnect() {
        val networkConnection = NetworkManager(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                Toast.makeText(applicationContext, "Connect internet", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "No connect internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }
    }

    private fun initBottomNavigation(){
        mainBinding.bottomNavigationViewMain.setOnItemSelectedListener { tab ->
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
                    navController.navigate(R.id.userFragment)
                }
            }
            true
        }
    }

}