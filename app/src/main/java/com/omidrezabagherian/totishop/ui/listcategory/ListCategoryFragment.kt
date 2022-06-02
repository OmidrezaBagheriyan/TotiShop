package com.omidrezabagherian.totishop.ui.listcategory

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentListCategoryBinding
import com.omidrezabagherian.totishop.ui.house.HouseAdapter
import com.omidrezabagherian.totishop.ui.house.HouseFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListCategoryFragment : Fragment(R.layout.fragment_list_category) {

    private lateinit var listCategoryBinding: FragmentListCategoryBinding
    private val listCategoryViewModel: ListCategoryViewModel by viewModels()
    private val listCategoryArgs: ListCategoryFragmentArgs by navArgs()
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listCategoryBinding = FragmentListCategoryBinding.bind(view)

        listCategoryBinding.textViewListCategory.text = listCategoryArgs.name

        productPopularityList()

    }

    private fun productPopularityList() {

        val listCategoryAdapter = ListCategoryAdapter(details = { product ->
            navController.navigate(
                ListCategoryFragmentDirections.actionListCategoryFragmentToDetailFragment(
                    product.id
                )
            )
        })

        listCategoryBinding.recyclerViewListCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        listCategoryViewModel.getProductCategoryList(listCategoryArgs.id)

        lifecycleScope.launch {
            listCategoryViewModel.productCategoryList.collect {
                listCategoryAdapter.submitList(it)
                Log.i("error", it.toString())
                listCategoryBinding.recyclerViewListCategory.adapter = listCategoryAdapter
            }
        }

    }

}