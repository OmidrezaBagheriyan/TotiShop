package com.omidrezabagherian.totishop.ui.bag

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.FragmentBagBinding
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.updateorder.Billing
import com.omidrezabagherian.totishop.domain.model.updateorder.LineItem
import com.omidrezabagherian.totishop.domain.model.updateorder.Shipping
import com.omidrezabagherian.totishop.domain.model.updateorder.UpdateOrder
import com.omidrezabagherian.totishop.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BagFragment : Fragment(R.layout.fragment_bag) {

    private lateinit var bagBinding: FragmentBagBinding
    private val bagViewModel: BagViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var bagSharedPreferences: SharedPreferences
    private lateinit var bagAdapter: BagAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bagBinding = FragmentBagBinding.bind(view)

        bagSharedPreferences =
            requireActivity().getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)

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
                showBag()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun totalPricePayInformation(lineItemList: List<com.omidrezabagherian.totishop.domain.model.order.LineItem>) {
        var price = 0.0
        for (i in lineItemList) {
            price += i.price
        }

        bagBinding.textViewBagPriceAll.text = "${price.toInt()} تومان"
    }

    private fun showPayInformation() {

        val bagSharedPreferencesEditor = bagSharedPreferences.edit()

        bagBinding.materialButtonContinuationPay.setOnClickListener {

            bagSharedPreferencesEditor.putInt(Values.ID_ORDER_SHARED_PREFERENCES, 0)
            bagSharedPreferencesEditor.commit()
            bagSharedPreferencesEditor.apply()

            val createOrder = CreateOrder(
                billing = com.omidrezabagherian.totishop.domain.model.createorder.Billing(
                    address_1 = bagSharedPreferences.getString(
                        Values.Address_SHARED_PREFERENCES,
                        ""
                    )
                        .toString(),
                    email = bagSharedPreferences.getString(Values.EMAIL_SHARED_PREFERENCES, "")
                        .toString(),
                    first_name = bagSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                        .toString(),
                    last_name = bagSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                        .toString(),
                    phone = bagSharedPreferences.getString(Values.PASSWORD_SHARED_PREFERENCES, "")
                        .toString()
                ),
                shipping = com.omidrezabagherian.totishop.domain.model.createorder.Shipping(
                    address_1 = bagSharedPreferences.getString(
                        Values.Address_SHARED_PREFERENCES,
                        ""
                    )
                        .toString(),
                    first_name = bagSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                        .toString(),
                    last_name = bagSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                        .toString(),
                ),
                line_items = emptyList(),
                shipping_lines = emptyList()
            )

            mainViewModel.setOrders(createOrder)

            if (bagSharedPreferences.getInt(Values.ID_ORDER_SHARED_PREFERENCES, 0) == 0) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        mainViewModel.setProductBagList.collect {
                            when (it) {
                                is ResultWrapper.Loading -> {
                                    bagBinding.recyclerViewBagShop.visibility = View.GONE
                                    bagBinding.cardViewBagPrice.visibility = View.GONE
                                    bagBinding.lottieAnimationViewErrorBag.visibility =
                                        View.INVISIBLE
                                    bagBinding.lottieAnimationViewLoadingBag.visibility =
                                        View.VISIBLE
                                    bagBinding.textViewErrorLoadingBag.text =
                                        "در حال بارگذاری"
                                    bagBinding.cardViewBagCheckingBag.visibility = View.VISIBLE
                                }
                                is ResultWrapper.Success -> {
                                    bagBinding.cardViewBagCheckingBag.visibility = View.GONE
                                    bagBinding.recyclerViewBagShop.visibility = View.VISIBLE
                                    bagBinding.cardViewBagPrice.visibility = View.VISIBLE
                                    bagBinding.textViewBagPriceAll.text = "0 تومان"
                                    bagSharedPreferencesEditor.putInt(
                                        Values.ID_ORDER_SHARED_PREFERENCES,
                                        it.value.id
                                    )
                                    bagSharedPreferencesEditor.commit()
                                    bagSharedPreferencesEditor.apply()
                                    bagAdapter.submitList(it.value.line_items)
                                }
                                is ResultWrapper.Error -> {
                                    bagBinding.recyclerViewBagShop.visibility = View.GONE
                                    bagBinding.cardViewBagPrice.visibility = View.GONE
                                    bagBinding.lottieAnimationViewErrorBag.visibility =
                                        View.VISIBLE
                                    bagBinding.lottieAnimationViewLoadingBag.visibility =
                                        View.INVISIBLE
                                    bagBinding.textViewErrorLoadingBag.text =
                                        "خطا در بارگذاری"
                                    bagBinding.cardViewBagCheckingBag.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private fun showBag() {
        bagViewModel.getOrders(
            bagSharedPreferences.getInt(
                Values.ID_ORDER_SHARED_PREFERENCES,
                0
            )
        )

        bagBinding.recyclerViewBagShop.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        bagAdapter = BagAdapter(add = {
            val updateOrder = UpdateOrder(
                billing = Billing(
                    address_1 = bagSharedPreferences.getString(
                        Values.Address_SHARED_PREFERENCES,
                        ""
                    )
                        .toString(),
                    email = bagSharedPreferences.getString(Values.EMAIL_SHARED_PREFERENCES, "")
                        .toString(),
                    first_name = bagSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                        .toString(),
                    last_name = bagSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                        .toString(),
                    phone = bagSharedPreferences.getString(Values.PASSWORD_SHARED_PREFERENCES, "")
                        .toString()
                ),
                shipping = Shipping(
                    address_1 = bagSharedPreferences.getString(
                        Values.Address_SHARED_PREFERENCES,
                        ""
                    )
                        .toString(),
                    first_name = bagSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                        .toString(),
                    last_name = bagSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                        .toString(),
                ),
                line_items = mutableListOf(LineItem(it.id, it.product_id, (it.quantity + 1))),
                shipping_lines = emptyList()
            )

            bagViewModel.editQuantityToOrders(
                bagSharedPreferences.getInt(
                    Values.ID_ORDER_SHARED_PREFERENCES,
                    0
                ), updateOrder
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    bagViewModel.putProductBagList.collect { order ->
                        when (order) {
                            is ResultWrapper.Loading -> {
                                Toast.makeText(
                                    requireContext(),
                                    "در حال اضافه شدن تعداد محصول",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is ResultWrapper.Success -> {
                                Toast.makeText(
                                    requireContext(),
                                    "به تعداد محصول اضافه شد",
                                    Toast.LENGTH_SHORT
                                ).show()
                                totalPricePayInformation(order.value.line_items)
                                bagAdapter.submitList(order.value.line_items)
                            }
                            is ResultWrapper.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "خطای در اضافه شدن تعداد محصول",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }, nag = {
            val updateOrder = UpdateOrder(
                billing = Billing(
                    address_1 = bagSharedPreferences.getString(
                        Values.Address_SHARED_PREFERENCES,
                        ""
                    )
                        .toString(),
                    email = bagSharedPreferences.getString(Values.EMAIL_SHARED_PREFERENCES, "")
                        .toString(),
                    first_name = bagSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                        .toString(),
                    last_name = bagSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                        .toString(),
                    phone = bagSharedPreferences.getString(Values.PASSWORD_SHARED_PREFERENCES, "")
                        .toString()
                ),
                shipping = Shipping(
                    address_1 = bagSharedPreferences.getString(
                        Values.Address_SHARED_PREFERENCES,
                        ""
                    )
                        .toString(),
                    first_name = bagSharedPreferences.getString(Values.NAME_SHARED_PREFERENCES, "")
                        .toString(),
                    last_name = bagSharedPreferences.getString(Values.FAMILY_SHARED_PREFERENCES, "")
                        .toString(),
                ),
                line_items = mutableListOf(LineItem(it.id, it.product_id, (it.quantity - 1))),
                shipping_lines = emptyList()
            )

            bagViewModel.editQuantityToOrders(
                bagSharedPreferences.getInt(
                    Values.ID_ORDER_SHARED_PREFERENCES,
                    0
                ), updateOrder
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    bagViewModel.putProductBagList.collect { order ->
                        when (order) {
                            is ResultWrapper.Loading -> {
                                Toast.makeText(
                                    requireContext(),
                                    "در حال کم شدن تعداد محصول",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is ResultWrapper.Success -> {
                                Toast.makeText(
                                    requireContext(),
                                    "تعداد محصول کم شد",
                                    Toast.LENGTH_SHORT
                                ).show()
                                totalPricePayInformation(order.value.line_items)
                                bagAdapter.submitList(order.value.line_items)
                            }
                            is ResultWrapper.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "خطای در کم شدن تعداد محصول",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        })

        bagBinding.recyclerViewBagShop.adapter = bagAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bagViewModel.getProductBagList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            bagBinding.recyclerViewBagShop.visibility = View.GONE
                            bagBinding.cardViewBagPrice.visibility = View.GONE
                            bagBinding.lottieAnimationViewErrorBag.visibility =
                                View.INVISIBLE
                            bagBinding.lottieAnimationViewLoadingBag.visibility =
                                View.VISIBLE
                            bagBinding.textViewErrorLoadingBag.text =
                                "در حال بارگذاری"
                            bagBinding.cardViewBagCheckingBag.visibility = View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            bagBinding.cardViewBagCheckingBag.visibility = View.GONE

                            bagBinding.recyclerViewBagShop.visibility = View.VISIBLE
                            bagBinding.cardViewBagPrice.visibility = View.VISIBLE

                            showPayInformation()
                            totalPricePayInformation(it.value.line_items)
                            bagAdapter.submitList(it.value.line_items)
                        }
                        is ResultWrapper.Error -> {
                            bagBinding.recyclerViewBagShop.visibility = View.GONE
                            bagBinding.cardViewBagPrice.visibility = View.GONE
                            bagBinding.lottieAnimationViewErrorBag.visibility =
                                View.VISIBLE
                            bagBinding.lottieAnimationViewLoadingBag.visibility =
                                View.INVISIBLE
                            bagBinding.textViewErrorLoadingBag.text =
                                "خطا در بارگذاری"
                            bagBinding.cardViewBagCheckingBag.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}
