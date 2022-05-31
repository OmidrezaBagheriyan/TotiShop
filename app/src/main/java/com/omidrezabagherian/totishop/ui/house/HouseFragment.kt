package com.omidrezabagherian.totishop.ui.house

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentHouseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HouseFragment : Fragment(R.layout.fragment_house) {

    private lateinit var houseBinding: FragmentHouseBinding
    private val houseViewModel: HouseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        houseBinding = FragmentHouseBinding.bind(view)

        val productsRatingMap = HashMap<String, String>().apply {
            put("orderby", "rating")
        }

        productDateList()
        productRatingList()
        productPopularityList()

    }

    private fun productDateList() {
        val houseAdapter = HouseAdapter(details = { product ->
            Toast.makeText(requireContext(), product.id.toString(), Toast.LENGTH_SHORT).show()
        })

        val productsDateMap = HashMap<String, String>().apply {
            put("orderby", "date")
        }

        houseBinding.recyclerViewHouseDateProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        houseViewModel.getProductDateList(productsDateMap)

        lifecycleScope.launch {
            houseViewModel.productDateList.collect {
                houseAdapter.submitList(it)
                houseBinding.recyclerViewHouseDateProduct.adapter = houseAdapter
            }
        }

    }

    private fun productRatingList() {
        val houseAdapter = HouseAdapter(details = { product ->
            Toast.makeText(requireContext(), product.id.toString(), Toast.LENGTH_SHORT).show()
        })

        val productsRatingMap = HashMap<String, String>().apply {
            put("orderby", "rating")
        }

        houseBinding.recyclerViewHouseRateProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        houseViewModel.getProductRatingList(productsRatingMap)

        lifecycleScope.launch {
            houseViewModel.productRatingList.collect {
                houseAdapter.submitList(it)
                houseBinding.recyclerViewHouseRateProduct.adapter = houseAdapter
            }
        }
    }

    private fun productPopularityList() {
        val houseAdapter = HouseAdapter(details = { product ->
            Toast.makeText(requireContext(), product.id.toString(), Toast.LENGTH_SHORT).show()
        })

        val productsPopularityMap = HashMap<String, String>().apply {
            put("orderby", "popularity")
        }

        houseBinding.recyclerViewHousePopularityProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        houseViewModel.getProductPopularityList(productsPopularityMap)

        lifecycleScope.launch {
            houseViewModel.productPopularityList.collect {
                houseAdapter.submitList(it)
                houseBinding.recyclerViewHousePopularityProduct.adapter = houseAdapter
            }
        }

    }
}