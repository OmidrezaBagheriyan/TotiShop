package com.omidrezabagherian.totishop.ui.category

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var categoryBinding: FragmentCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = CategoryAdapter(details = { category ->
            navController.navigate(
                CategoryFragmentDirections.actionCategoryFragmentToListCategoryFragment(
                    category.name,
                    category.id
                )
            )
        })

        categoryBinding = FragmentCategoryBinding.bind(view)

        categoryBinding.recyclerViewCategory.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        categoryViewModel.getCategoryList()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.categoryList.collect {
                    categoryAdapter.submitList(it)
                    categoryBinding.recyclerViewCategory.adapter = categoryAdapter
                }
            }
        }

    }
}