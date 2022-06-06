package com.omidrezabagherian.totishop.ui.house

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.databinding.FragmentHouseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HouseFragment : Fragment(R.layout.fragment_house) {

    private lateinit var houseBinding: FragmentHouseBinding
    private val houseViewModel: HouseViewModel by viewModels()
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        houseBinding = FragmentHouseBinding.bind(view)

        searchPage()
        checkInternet()

    }

    private fun searchPage() {
        houseBinding.imageViewHouseSearch.setOnClickListener {
            navController.navigate(HouseFragmentDirections.actionHouseFragmentToSearchFragment())
        }
    }

    private fun dialogCheckInternet() {
        val dialog = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_check_internet, null)
        val buttonCheckInternet: Button = dialogView.findViewById(R.id.buttonCheckInternet)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        val customDialog = dialog.create()
        customDialog.show()

        buttonCheckInternet.setOnClickListener {
            customDialog.dismiss()
            checkInternet()
        }
    }

    private fun checkInternet() {
        val networkConnection = NetworkManager(requireContext())
        networkConnection.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                productDateList()
                productRatingList()
                productPopularityList()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun productDateList() {
        val houseAdapter = HouseAdapter(details = { product ->
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToDetailFragment(
                    product.id
                )
            )
        })

        val productsDateMap = HashMap<String, String>().apply {
            put("orderby", "date")
        }

        houseBinding.recyclerViewHouseDateProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        houseViewModel.getProductDateList(productsDateMap)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseViewModel.productDateList.collect {
                    houseAdapter.submitList(it)
                    houseBinding.recyclerViewHouseDateProduct.adapter = houseAdapter
                }
            }
        }

    }

    private fun productRatingList() {
        val houseAdapter = HouseAdapter(details = { product ->
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToDetailFragment(
                    product.id
                )
            )
        })

        val productsRatingMap = HashMap<String, String>().apply {
            put("orderby", "rating")
        }

        houseBinding.recyclerViewHouseRateProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        houseViewModel.getProductRatingList(productsRatingMap)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseViewModel.productRatingList.collect {
                    houseAdapter.submitList(it)
                    houseBinding.recyclerViewHouseRateProduct.adapter = houseAdapter
                }
            }
        }

    }

    private fun productPopularityList() {
        val houseAdapter = HouseAdapter(details = { product ->
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToDetailFragment(
                    product.id
                )
            )
        })

        val productsPopularityMap = HashMap<String, String>().apply {
            put("orderby", "popularity")
        }

        houseBinding.recyclerViewHousePopularityProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        houseViewModel.getProductPopularityList(productsPopularityMap)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseViewModel.productPopularityList.collect {
                    houseAdapter.submitList(it)
                    houseBinding.recyclerViewHousePopularityProduct.adapter = houseAdapter
                }
            }
        }

    }
}