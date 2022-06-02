package com.omidrezabagherian.totishop.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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

        checkConnect()

        initSplashScreen()


        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mainBinding.root)
    }

    private fun checkConnect() {
        val networkConnection = NetworkManager(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                initBottomNavigation()
            } else {
                Toast.makeText(applicationContext, "No internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dialogConnectInternet(context: Context) {

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_check_internet, null)
        dialogBuilder.setView(dialogView)

        val buttonCheckInternet: Button = dialogView.findViewById(R.id.buttonCheckInternet)
        val alertDialog = dialogBuilder.create()

        buttonCheckInternet.setOnClickListener {
            checkConnect()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }
    }

    private fun initBottomNavigation() {
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