package com.omidrezabagherian.totishop.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Build.ID
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.core.TotiShopWorker
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.ActivityMainBinding
import com.omidrezabagherian.totishop.domain.model.createorder.Billing
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.createorder.Shipping
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mainSharedPreferences: SharedPreferences
    private lateinit var navController: NavController
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSplashScreen()

        mainSharedPreferences = getSharedPreferences(Values.SHARED_PREFERENCES, MODE_PRIVATE)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        initBottomNavigation()

        setOrder()

        workManagerInit()

        setContentView(mainBinding.root)
    }

    private fun workManagerInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel: NotificationChannel = NotificationChannel(
                Values.CHANNEL_ID,
                Values.CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(notificationChannel)
        }

        periodicWorkRequest = PeriodicWorkRequestBuilder<TotiShopWorker>(3, TimeUnit.HOURS).build()
        workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork(
            ID,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }
    }

    private fun setOrder() {
        val createOrder = CreateOrder(
            billing = Billing(
                address_1 = mainSharedPreferences.getString(Values.Address_SHARED_PREFERENCES, "")
                    .toString(),
                email = mainSharedPreferences.getString(Values.EMAIL_SHARED_PREFERENCES, "")
                    .toString(),
                first_name = mainSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                    .toString(),
                last_name = mainSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                    .toString(),
                phone = mainSharedPreferences.getString(Values.PASSWORD_SHARED_PREFERENCES, "")
                    .toString()
            ),
            shipping = Shipping(
                address_1 = mainSharedPreferences.getString(Values.Address_SHARED_PREFERENCES, "")
                    .toString(),
                first_name = mainSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                    .toString(),
                last_name = mainSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                    .toString(),
            ),
            line_items = emptyList(),
            shipping_lines = emptyList()
        )

        mainViewModel.setOrders(createOrder)

        val mainSharedPreferencesEditor = mainSharedPreferences.edit()

        if (mainSharedPreferences.getInt(Values.ID_ORDER_SHARED_PREFERENCES, 0) == 0) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainViewModel.setProductBagList.collect {
                        when (it) {
                            is ResultWrapper.Loading -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "درحال ساخت سبد خرید",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is ResultWrapper.Success -> {
                                mainSharedPreferencesEditor.putInt(
                                    Values.ID_ORDER_SHARED_PREFERENCES,
                                    it.value.id
                                )
                                mainSharedPreferencesEditor.commit()
                                mainSharedPreferencesEditor.apply()
                            }
                            is ResultWrapper.Error -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "مشکل ساخت سبد خرید",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain)
                    as NavHostFragment
        navController = navHostFragment.navController

        mainBinding.bottomNavigationViewMain.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destenation, _ ->
            if (destenation.id == R.id.detailFragment || destenation.id == R.id.searchFragment || destenation.id == R.id.reviewFragment) {
                mainBinding.bottomNavigationViewMain.visibility = View.GONE
            } else {
                mainBinding.bottomNavigationViewMain.visibility = View.VISIBLE
            }
        }

        mainBinding.bottomNavigationViewMain.setOnItemSelectedListener { tab ->
            val fragmentMain = navController.currentDestination?.id?.plus(1)
            val fragmentNext = tab.itemId
            if (fragmentMain != fragmentNext) {
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