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

        bagViewModel.getOrders(
            bagSharedPreferences.getInt(
                Values.ID_ORDER_SHARED_PREFERENCES,
                0
            )
        )

        bagBinding.recyclerViewBagShop.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val bagAdapter = BagAdapter(delete = {

        })

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