package com.omidrezabagherian.totishop.ui.house

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omidrezabagherian.totishop.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HouseFragment : Fragment(R.layout.fragment_house) {

    private val houseViewModel:HouseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productsMap = hashMapOf<String,String>().apply {
            put("products","popularity")
        }
        houseViewModel.getProductList(productsMap)


        Log.i("Log1",houseViewModel.productList.value.toString())
        Log.i("Log2",houseViewModel.productError.value.toString())

    }
}