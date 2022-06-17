package com.omidrezabagherian.totishop.ui.house

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.databinding.FragmentHouseBinding
import com.omidrezabagherian.totishop.core.Values.DELAY_MS
import com.omidrezabagherian.totishop.core.Values.PERIOD_MS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

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

    private fun sliderImage() {
        houseViewModel.getSliderImageList(608)

        var currentPage = 0;

        val sliderAdapter = SliderAdapter(requireContext())
        houseBinding.viewPagerHouseSliderImage.adapter = sliderAdapter

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post(Runnable {
                    if (currentPage == sliderAdapter.count - 1) {
                        currentPage = 0
                    }
                    houseBinding.viewPagerHouseSliderImage.setCurrentItem(
                        currentPage++,
                        true
                    )
                })
            }
        }, DELAY_MS, PERIOD_MS)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseViewModel.sliderImage.collect { images ->

                    sliderAdapter.setImages(images.images)

                }
            }
        }


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
                sliderImage()
                productDateList()
                productRatingList()
                productPopularityList()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun productDateList() {
        val word = "date"
        houseBinding.textViewHouseDateProductMore.setOnClickListener {
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToListProductFragment(word)
            )
        }

        val houseAdapter = HouseAdapter(details = { product ->
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToDetailFragment(
                    product.id
                )
            )
        })

        val productsDateMap = HashMap<String, String>().apply {
            put("orderby", word)
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
        val word = "rating"
        houseBinding.textViewHouseRateProductMore.setOnClickListener {
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToListProductFragment(word)
            )
        }

        val houseAdapter = HouseAdapter(details = { product ->
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToDetailFragment(
                    product.id
                )
            )
        })

        val productsRatingMap = HashMap<String, String>().apply {
            put("orderby", word)
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
        val word = "popularity"
        houseBinding.textViewHousePopularityProductMore.setOnClickListener {
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToListProductFragment(word)
            )
        }

        val houseAdapter = HouseAdapter(details = { product ->
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToDetailFragment(
                    product.id
                )
            )
        })

        val productsPopularityMap = HashMap<String, String>().apply {
            put("orderby", word)
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