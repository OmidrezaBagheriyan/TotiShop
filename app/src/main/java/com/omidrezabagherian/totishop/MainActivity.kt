package com.omidrezabagherian.totishop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.omidrezabagherian.totishop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        mainBinding.bottomNavigationViewMain.setOnItemSelectedListener { tab ->
            when (tab.itemId) {
                R.id.houseTab -> {
                    Toast.makeText(applicationContext,R.string.text_tab_house,Toast.LENGTH_SHORT).show()
                }
                R.id.categoryTab -> {
                    Toast.makeText(applicationContext,R.string.text_tab_category,Toast.LENGTH_SHORT).show()
                }
                R.id.bagTab -> {
                    Toast.makeText(applicationContext,R.string.text_tab_bag,Toast.LENGTH_SHORT).show()
                }
                R.id.userTab -> {
                    Toast.makeText(applicationContext,R.string.text_tab_user,Toast.LENGTH_SHORT).show()
                }
            }

            true
        }

        setContentView(mainBinding.root)
    }
}