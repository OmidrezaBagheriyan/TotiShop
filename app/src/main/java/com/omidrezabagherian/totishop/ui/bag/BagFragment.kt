package com.omidrezabagherian.totishop.ui.bag

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.Values
import com.omidrezabagherian.totishop.databinding.FragmentBagBinding
import com.omidrezabagherian.totishop.domain.model.createorder.Billing
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.createorder.Shipping
import com.omidrezabagherian.totishop.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BagFragment : Fragment(R.layout.fragment_bag) {

    private lateinit var bagBinding: FragmentBagBinding
    private val bagViewModel: BagViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var bagSharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bagBinding = FragmentBagBinding.bind(view)

        bagSharedPreferences =
            requireActivity().getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val bagSharedPreferencesEditor = bagSharedPreferences.edit()

        mainViewModel.getCustomer(bagSharedPreferences.getInt(Values.ID_SHARED_PREFERENCES, 0))

        val createOrder = CreateOrder(
            billing = Billing(
                address_1 = "کرج - فردیس - خیابان داوری - کوچه عباسی - پلاک ۳۰ - واحد ۳",
                email = "omidrezabagherian@yahoo.com",
                first_name = "اميدرضا",
                last_name = "باقریان اسفندانی",
                phone = "09028501761"
            ),
            shipping = Shipping(
                address_1 = "کرج - فردیس - خیابان داوری - کوچه عباسی - پلاک ۳۰ - واحد ۳",
                first_name = "اميدرضا",
                last_name = "باقریان اسفندانی",
            ),
            line_items = emptyList(),
            shipping_lines = emptyList()
        )

        bagViewModel.setOrders(createOrder)

        bagBinding.recyclerViewBagShop.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val bagAdapter = BagAdapter(delete = {

        })

        if (bagSharedPreferences.getInt(Values.ID_ORDER_SHARED_PREFERENCES, 0) == 0) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    bagViewModel.setProductBagList.collect {
                        bagAdapter.submitList(it.line_items)
                        bagBinding.recyclerViewBagShop.adapter = bagAdapter

                        Log.i(
                            "lineItems",
                            it.line_items.toString()
                        )

                        bagSharedPreferencesEditor.putInt(Values.ID_ORDER_SHARED_PREFERENCES, it.id)
                        bagSharedPreferencesEditor.commit()
                        bagSharedPreferencesEditor.apply()

                        Log.i(
                            "idOrder0",
                            it.id.toString()
                        )
                    }
                }
            }
        } else {
            bagViewModel.getOrders(
                bagSharedPreferences.getInt(
                    Values.ID_ORDER_SHARED_PREFERENCES,
                    0
                )
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    bagViewModel.getProductBagList.collect {
                        Log.i(
                            "lineItems",
                            it.line_items.toString()
                        )

                        bagAdapter.submitList(it.line_items)
                        bagBinding.recyclerViewBagShop.adapter = bagAdapter

                        Log.i(
                            "idOrder0",
                            it.id.toString()
                        )
                    }
                }
            }
        }


    }
}