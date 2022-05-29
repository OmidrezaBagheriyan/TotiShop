package com.omidrezabagherian.totishop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val navController by lazy {
        findNavController(R.id.fragmentContainerViewMain)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        installSplashScreen().apply {
            setKeepOnScreenCondition{
                mainViewModel.isLoading.value
            }
        }

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

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

        setContentView(mainBinding.root)
    }
}