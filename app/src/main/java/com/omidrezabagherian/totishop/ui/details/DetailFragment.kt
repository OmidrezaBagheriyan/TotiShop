package com.omidrezabagherian.totishop.ui.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_details) {

    private lateinit var detailsBinding: FragmentDetailsBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private val detailArgs: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsBinding = FragmentDetailsBinding.bind(view)

        detailViewModel.getProduct(detailArgs.id)

        val categoryAdapter = CategoryDetailAdapter(details = { category ->
            Toast.makeText(requireContext(), category.name, Toast.LENGTH_SHORT).show()
        })

        val tagAdapter = TagDetailAdapter(details = { tag ->
            Toast.makeText(requireContext(), tag.name, Toast.LENGTH_SHORT).show()
        })

        detailsBinding.recyclerViewDetailCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        detailsBinding.recyclerViewDetailTags.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        lifecycleScope.launch {
            detailViewModel.productValues.collect { product ->
                detailsBinding.textViewDetailName.text = product.name
                detailsBinding.textViewDetailNumberRate.text =
                    "امتیاز این محصول: ${product.rating_count}"
                categoryAdapter.submitList(product.categories)
                detailsBinding.recyclerViewDetailCategories.adapter = categoryAdapter
                tagAdapter.submitList(product.tags)
                detailsBinding.recyclerViewDetailTags.adapter = tagAdapter
                detailsBinding.textViewDetailPrice.text = "${product.price} تومان"
                detailsBinding.textViewDetailDescription.text = product.description
            }
        }

    }

}