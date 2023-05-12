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
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.util.NetworkManager
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.databinding.FragmentHouseBinding
import com.omidrezabagherian.totishop.util.Values.DELAY_MS
import com.omidrezabagherian.totishop.util.Values.PERIOD_MS
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

    private fun searchPage() {
        houseBinding.imageViewHouseSearch.setOnClickListener {
            navController.navigate(HouseFragmentDirections.actionHouseFragmentToSearchFragment())
        }
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
                houseViewModel.sliderImage.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            houseBinding.viewPagerHouseSliderImage.visibility = View.GONE
                            houseBinding.lottieAnimationViewErrorSliderImageHouse.visibility =
                                View.INVISIBLE
                            houseBinding.lottieAnimationViewLoadingSliderImageHouse.visibility =
                                View.VISIBLE
                            houseBinding.textViewErrorLoadingSliderImageHouse.text =
                                "در حال بارگذاری"
                            houseBinding.cardViewSliderImageCheckingSliderImage.visibility =
                                View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            houseBinding.viewPagerHouseSliderImage.visibility = View.VISIBLE
                            houseBinding.cardViewSliderImageCheckingSliderImage.visibility =
                                View.GONE
                            houseBinding.lottieAnimationViewErrorSliderImageHouse.visibility =
                                View.GONE
                            houseBinding.lottieAnimationViewLoadingSliderImageHouse.visibility =
                                View.GONE
                            houseBinding.textViewErrorLoadingSliderImageHouse.text = ""
                            sliderAdapter.setImages(it.value.images)
                        }
                        is ResultWrapper.Error -> {
                            houseBinding.viewPagerHouseSliderImage.visibility = View.GONE
                            houseBinding.lottieAnimationViewErrorSliderImageHouse.visibility =
                                View.VISIBLE
                            houseBinding.lottieAnimationViewLoadingSliderImageHouse.visibility =
                                View.INVISIBLE
                            houseBinding.textViewErrorLoadingSliderImageHouse.text =
                                "خطا در بارگذاری"
                            houseBinding.cardViewSliderImageCheckingSliderImage.visibility =
                                View.VISIBLE
                        }
                    }
                }
            }
        }


    }

    private fun productDateList() {
        val word = "date"
        houseBinding.textViewHouseDateProductMore.setOnClickListener {
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToListProductFragment(
                    word,
                    "جدیدترین محصولات"
                )
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
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)

        houseViewModel.getProductDateList(productsDateMap)
        houseBinding.recyclerViewHouseDateProduct.adapter = houseAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseViewModel.productDateList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            houseBinding.lottieAnimationViewErrorDateHouse.visibility =
                                View.INVISIBLE
                            houseBinding.lottieAnimationViewLoadingDateHouse.visibility =
                                View.VISIBLE
                            houseBinding.textViewErrorLoadingDateHouse.text = "در حال بارگذاری"
                            houseBinding.cardViewHouseCheckingDate.visibility = View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            houseBinding.cardViewHouseCheckingDate.visibility = View.GONE
                            houseBinding.lottieAnimationViewErrorDateHouse.visibility = View.GONE
                            houseBinding.lottieAnimationViewLoadingDateHouse.visibility = View.GONE
                            houseBinding.textViewErrorLoadingDateHouse.text = ""
                            houseAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            houseBinding.lottieAnimationViewErrorDateHouse.visibility = View.VISIBLE
                            houseBinding.lottieAnimationViewLoadingDateHouse.visibility =
                                View.INVISIBLE
                            houseBinding.textViewErrorLoadingDateHouse.text = "خطا در بارگذاری"
                            houseBinding.cardViewHouseCheckingDate.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

    }

    private fun productRatingList() {
        val word = "rating"
        houseBinding.textViewHouseRateProductMore.setOnClickListener {
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToListProductFragment(
                    word,
                    "پربازدیدترین محصولات"
                )
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
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)

        houseViewModel.getProductRatingList(productsRatingMap)
        houseBinding.recyclerViewHouseRateProduct.adapter = houseAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseViewModel.productRatingList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            houseBinding.lottieAnimationViewErrorRateHouse.visibility =
                                View.INVISIBLE
                            houseBinding.lottieAnimationViewLoadingRateHouse.visibility =
                                View.VISIBLE
                            houseBinding.textViewErrorLoadingRateHouse.text = "در حال بارگذاری"
                            houseBinding.cardViewHouseCheckingRate.visibility = View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            houseBinding.cardViewHouseCheckingRate.visibility = View.GONE
                            houseBinding.lottieAnimationViewErrorRateHouse.visibility = View.GONE
                            houseBinding.lottieAnimationViewLoadingRateHouse.visibility = View.GONE
                            houseBinding.textViewErrorLoadingRateHouse.text = ""
                            houseAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            houseBinding.lottieAnimationViewErrorRateHouse.visibility = View.VISIBLE
                            houseBinding.lottieAnimationViewLoadingRateHouse.visibility =
                                View.INVISIBLE
                            houseBinding.textViewErrorLoadingRateHouse.text = "خطا در بارگذاری"
                            houseBinding.cardViewHouseCheckingRate.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

    }

    private fun productPopularityList() {
        val word = "popularity"
        houseBinding.textViewHousePopularityProductMore.setOnClickListener {
            navController.navigate(
                HouseFragmentDirections.actionHouseFragmentToListProductFragment(
                    word,
                    "محبوبیت محصولات"
                )
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
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)

        houseViewModel.getProductPopularityList(productsPopularityMap)
        houseBinding.recyclerViewHousePopularityProduct.adapter = houseAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseViewModel.productPopularityList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            houseBinding.lottieAnimationViewErrorPopularityHouse.visibility =
                                View.INVISIBLE
                            houseBinding.lottieAnimationViewLoadingPopularityHouse.visibility =
                                View.VISIBLE
                            houseBinding.textViewErrorLoadingPopularityHouse.text =
                                "در حال بارگذاری"
                            houseBinding.cardViewHouseCheckingPopularity.visibility = View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            houseBinding.cardViewHouseCheckingPopularity.visibility = View.GONE
                            houseBinding.lottieAnimationViewErrorPopularityHouse.visibility =
                                View.GONE
                            houseBinding.lottieAnimationViewLoadingPopularityHouse.visibility =
                                View.GONE
                            houseBinding.textViewErrorLoadingPopularityHouse.text = ""
                            houseAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            houseBinding.lottieAnimationViewErrorPopularityHouse.visibility =
                                View.VISIBLE
                            houseBinding.lottieAnimationViewLoadingPopularityHouse.visibility =
                                View.INVISIBLE
                            houseBinding.textViewErrorLoadingPopularityHouse.text =
                                "خطا در بارگذاری"
                            houseBinding.cardViewHouseCheckingPopularity.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

    }
}