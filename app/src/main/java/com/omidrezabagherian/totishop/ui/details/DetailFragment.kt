package com.omidrezabagherian.totishop.ui.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
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

        lifecycleScope.launch {
            detailViewModel.productValues.collect {
                Log.i("r", it.toString())
            }
        }

    }

}