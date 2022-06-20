package com.omidrezabagherian.totishop.ui.details

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.FragmentDetailsBinding
import com.omidrezabagherian.totishop.domain.model.createorder.Billing
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.createorder.Shipping
import com.omidrezabagherian.totishop.domain.model.order.LineItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_details) {

    private lateinit var detailsBinding: FragmentDetailsBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private val detailArgs: DetailFragmentArgs by navArgs()
    private lateinit var detailSharedPreferences: SharedPreferences
    private val navController by lazy {
        findNavController()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsBinding = FragmentDetailsBinding.bind(view)

        detailSharedPreferences = requireActivity().getSharedPreferences(
            Values.SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

        checkInternet()

    }

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkInternet() {
        val networkConnection = NetworkManager(requireContext())
        networkConnection.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                detailViewModel.getProduct(detailArgs.id)
                getListItems()
                showDetails()
            } else {
                dialogCheckInternet()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDetails() {
        val categoryAdapter = CategoryDetailAdapter(details = { category ->
            navController.navigate(
                DetailFragmentDirections.actionDetailFragmentToListCategoryFragment(
                    category.name,
                    category.id
                )
            )
        })

        val tagAdapter = TagDetailAdapter(details = { tag ->
            Toast.makeText(requireContext(), tag.name, Toast.LENGTH_SHORT).show()
        })

        detailsBinding.recyclerViewDetailCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        detailsBinding.recyclerViewDetailTags.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.productValues.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                            detailsBinding.cardViewPrice.visibility = View.GONE
                            detailsBinding.nestedScrollViewDetails.visibility = View.GONE
                            detailsBinding.lottieAnimationViewErrorDetails.visibility =
                                View.GONE
                            detailsBinding.lottieAnimationViewLoadingDetails.visibility =
                                View.VISIBLE
                            detailsBinding.textViewErrorLoadingDetails.text =
                                "در حال بارگذاری"
                            detailsBinding.cardViewDetailsCheckingDetails.visibility =
                                View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                            detailsBinding.cardViewPrice.visibility = View.VISIBLE
                            detailsBinding.nestedScrollViewDetails.visibility = View.VISIBLE
                            detailsBinding.cardViewDetailsCheckingDetails.visibility =
                                View.GONE
                            detailsBinding.lottieAnimationViewErrorDetails.visibility =
                                View.GONE
                            detailsBinding.lottieAnimationViewLoadingDetails.visibility =
                                View.GONE
                            detailsBinding.textViewErrorLoadingDetails.text = ""
                            val imageAdapter = ImageAdapter(requireContext(), it.value.images)
                            detailsBinding.viewPagerDetailsImages.adapter = imageAdapter
                            detailsBinding.textViewDetailName.text = it.value.name
                            detailsBinding.textViewDetailNumberRate.text =
                                " ${it.value.rating_count} امتیاز /"
                            detailsBinding.ratingDetailNumberRate.rating =
                                it.value.rating_count.toFloat()
                            categoryAdapter.submitList(it.value.categories)
                            detailsBinding.recyclerViewDetailCategories.adapter = categoryAdapter
                            tagAdapter.submitList(it.value.tags)
                            detailsBinding.recyclerViewDetailTags.adapter = tagAdapter
                            detailsBinding.textViewDetailPrice.text = "${it.value.price} تومان"
                            detailsBinding.textViewDetailDescription.text =
                                Html.fromHtml(it.value.description, 0)
                        }
                        is ResultWrapper.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                            detailsBinding.cardViewPrice.visibility = View.GONE
                            detailsBinding.nestedScrollViewDetails.visibility = View.GONE
                            detailsBinding.lottieAnimationViewErrorDetails.visibility =
                                View.VISIBLE
                            detailsBinding.lottieAnimationViewLoadingDetails.visibility =
                                View.INVISIBLE
                            detailsBinding.textViewErrorLoadingDetails.text =
                                "خطا در بارگذاری"
                            detailsBinding.cardViewDetailsCheckingDetails.visibility =
                                View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun getListItems() {
        detailViewModel.getOrders(
            detailSharedPreferences.getInt(
                Values.ID_ORDER_SHARED_PREFERENCES,
                0
            )
        )

        val value = mutableListOf<LineItem>()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.getProductBagList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> { }
                        is ResultWrapper.Success -> {
                            value.addAll(it.value.line_items)

                            for (i in it.value.line_items) {
                                if (i.product_id == detailArgs.id) {
                                    detailsBinding.buttonDetailAddToBag.visibility = View.GONE
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            Toast.makeText(requireContext(), "مشکل در دریافت لیست سبد خرید", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        detailsBinding.buttonDetailAddToBag.setOnClickListener {
            val email = detailSharedPreferences.getString(Values.EMAIL_SHARED_PREFERENCES, "")
            val password = detailSharedPreferences.getString(Values.PASSWORD_SHARED_PREFERENCES, "")

            if (email!!.isNotEmpty() && password!!.isNotEmpty()) {
                val addValue =
                    mutableListOf<com.omidrezabagherian.totishop.domain.model.createorder.LineItem>()

                addValue.add(
                    com.omidrezabagherian.totishop.domain.model.createorder.LineItem(
                        detailArgs.id,
                        1
                    )
                )

                val createOrder = CreateOrder(
                    billing = Billing(
                        address_1 = detailSharedPreferences.getString(
                            Values.Address_SHARED_PREFERENCES,
                            ""
                        )
                            .toString(),
                        email = detailSharedPreferences.getString(
                            Values.EMAIL_SHARED_PREFERENCES,
                            ""
                        )
                            .toString(),
                        first_name = detailSharedPreferences.getString(
                            Values.NAME_SHARED_PREFERENCES,
                            ""
                        )
                            .toString(),
                        last_name = detailSharedPreferences.getString(
                            Values.FAMILY_SHARED_PREFERENCES,
                            ""
                        )
                            .toString(),
                        phone = detailSharedPreferences.getString(
                            Values.PASSWORD_SHARED_PREFERENCES,
                            ""
                        )
                            .toString()
                    ),
                    shipping = Shipping(
                        address_1 = detailSharedPreferences.getString(
                            Values.Address_SHARED_PREFERENCES,
                            ""
                        )
                            .toString(),
                        first_name = detailSharedPreferences.getString(
                            Values.NAME_SHARED_PREFERENCES,
                            ""
                        )
                            .toString(),
                        last_name = detailSharedPreferences.getString(
                            Values.FAMILY_SHARED_PREFERENCES,
                            ""
                        )
                            .toString(),
                    ),
                    line_items = addValue,
                    shipping_lines = emptyList()
                )

                detailViewModel.putOrders(
                    detailSharedPreferences.getInt(
                        Values.ID_ORDER_SHARED_PREFERENCES,
                        0
                    ), createOrder
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        detailViewModel.putProductBagList.collect {
                            Log.i("logLineItem", it.toString())
                        }
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "لطفا وارد حساب کاربری خود شوید",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

}