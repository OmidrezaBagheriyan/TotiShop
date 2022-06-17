package com.omidrezabagherian.totishop.ui.search

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.databinding.FragmentSearchBinding
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.HashMap

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchBinding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val navController by lazy {
        findNavController()
    }
    private var orderby = "price"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBinding = FragmentSearchBinding.bind(view)

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
                searchView()
                filterSearch()
                sortSearch()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun filterSearch() {
        searchBinding.imageViewSearchFilter.setOnClickListener {
            Toast.makeText(requireContext(), "فیلتر", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sortSearch() {
        searchBinding.imageViewSearchSorting.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.dialog_orderby, null)
            val buttonCheckInternet: Button = dialogView.findViewById(R.id.buttonCheckOrderBy)
            val radioGroup: RadioGroup = dialogView.findViewById(R.id.radioGroupOrderBy)
            dialog.setView(dialogView)
            dialog.setCancelable(false)
            val customDialog = dialog.create()
            customDialog.show()

            buttonCheckInternet.setOnClickListener {

                orderby = when (radioGroup.checkedRadioButtonId) {
                    R.id.orderByDate -> {
                        radioGroup
                        "date"
                    }
                    R.id.orderByID -> {
                        "id"
                    }
                    R.id.orderByTitle -> {
                        "title"
                    }
                    R.id.orderByPrice -> {
                        "price"
                    }
                    R.id.orderByPopularity -> {
                        "popularity"
                    }
                    R.id.orderByRating -> {
                        "rating"
                    }
                    else -> {
                        "price"
                    }
                }

                customDialog.dismiss()
                checkInternet()
            }
        }
    }

    private fun searchView() {
        searchBinding.searchViewSearchProduct.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                searchProduct(query.toString(), orderby)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun searchProduct(query: String, orderby: String) {
        val searchAdapter = SearchAdapter(details = { product ->
            navController.navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                    product.id
                )
            )
        })

        searchViewModel.getProductCategoryList(query, orderby)

        searchBinding.recyclerViewSearchProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        searchBinding.recyclerViewSearchProduct.adapter = searchAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.productSearchList.collect {
                    searchAdapter.submitList(it)
                }
            }
        }

    }

}