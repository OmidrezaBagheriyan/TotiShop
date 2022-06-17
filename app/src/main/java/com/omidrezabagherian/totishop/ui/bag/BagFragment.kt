package com.omidrezabagherian.totishop.ui.bag

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
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
    private val value = mutableListOf<com.omidrezabagherian.totishop.domain.model.order.LineItem>()

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

    private fun showPayInformation(lineItemList: List<com.omidrezabagherian.totishop.domain.model.order.LineItem>) {

        val bagSharedPreferencesEditor = bagSharedPreferences.edit()

        var price = 0.0
        for (i in lineItemList) {
            price += i.price
        }

        bagBinding.textViewBagPriceAll.text = "${price.toInt().toString()} تومان"

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
                            bagSharedPreferencesEditor.putInt(
                                Values.ID_ORDER_SHARED_PREFERENCES,
                                it.id
                            )
                            bagSharedPreferencesEditor.commit()
                            bagSharedPreferencesEditor.apply()
                        }
                    }
                }
            }

        }
    }

    private fun showBag() {
        mainViewModel.getCustomer(bagSharedPreferences.getInt(Values.ID_SHARED_PREFERENCES, 0))

        bagViewModel.getOrders(
            bagSharedPreferences.getInt(
                Values.ID_ORDER_SHARED_PREFERENCES,
                0
            )
        )

        bagBinding.recyclerViewBagShop.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val bagAdapter = BagAdapter(add = {
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
                        value.clear()
                        value.addAll(order.line_items)
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
                        if (order.line_items.isNotEmpty()) {

                        }
                    }
                }
            }
        })

        bagBinding.recyclerViewBagShop.adapter = bagAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bagViewModel.getProductBagList.collect { order ->
                    showPayInformation(order.line_items)
                    bagAdapter.submitList(order.line_items)
                }
            }
        }
    }
}