package com.omidrezabagherian.totishop.ui.category

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var categoryBinding: FragmentCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = CategoryAdapter(details = { category ->
            Toast.makeText(requireContext(), category.name, Toast.LENGTH_SHORT).show()
        })

        categoryBinding = FragmentCategoryBinding.bind(view)

        categoryBinding.recyclerViewCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        categoryViewModel.getCategoryList()

        lifecycleScope.launch {
            categoryViewModel.categoryList.collect {
                categoryAdapter.submitList(it)
                categoryBinding.recyclerViewCategory.adapter = categoryAdapter
            }
        }

    }
}