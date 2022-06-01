package com.omidrezabagherian.totishop.ui.category

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var categoryBinding: FragmentCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryBinding = FragmentCategoryBinding.bind(view)

        categoryViewModel.getCategoryList()

        Log.i("error", categoryViewModel.categoryList.value.toString())

    }
}