package com.omidrezabagherian.totishop.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.omidrezabagherian.totishop.R
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