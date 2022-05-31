package com.omidrezabagherian.totishop.ui.house

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentHouseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HouseFragment : Fragment(R.layout.fragment_house) {

    private lateinit var houseBinding: FragmentHouseBinding
    private val houseViewModel: HouseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        houseBinding = FragmentHouseBinding.bind(view)

        val productsMap1 = HashMap<String, String>().apply {
            put("orderby", "date")
        }
        val productsMap2 = HashMap<String, String>().apply {
            put("orderby", "popularity")
        }
        val productsMap3 = HashMap<String, String>().apply {
            put("orderby", "rating")
        }

        houseViewModel.getProductList(productsMap1)

        houseViewModel.productList.observe(viewLifecycleOwner) {
            Log.i("Log1", it.toString())
        }

        houseViewModel.getProductList(productsMap2)
        houseViewModel.productList.observe(viewLifecycleOwner) {
            Log.i("Log2", it.toString())
        }

        houseViewModel.getProductList(productsMap3)
        houseViewModel.productList.observe(viewLifecycleOwner) {
            Log.i("Log3", it.toString())
        }

    }
}