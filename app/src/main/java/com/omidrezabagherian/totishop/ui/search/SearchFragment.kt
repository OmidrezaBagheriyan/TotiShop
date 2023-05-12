package com.omidrezabagherian.totishop.ui.search

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.util.NetworkManager
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.util.Values.ID_FILTER_SHARED_PREFERENCES
import com.omidrezabagherian.totishop.util.Values.SHARED_PREFERENCES
import com.omidrezabagherian.totishop.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchBinding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchSharedPreferences: SharedPreferences
    private val navController by lazy {
        findNavController()
    }
    private var search: String = ""
    private var orderby = "price"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBinding = FragmentSearchBinding.bind(view)

        searchSharedPreferences =
            requireActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
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
            dialog.setView(dialogView)
            dialog.setCancelable(false)
            val customDialog = dialog.create()
            customDialog.show()
            val radioGroupOrder: RadioGroup = dialogView.findViewById(R.id.radioGroupOrderBy)
            val radioButtonOrderByDate: RadioButton = dialogView.findViewById(R.id.orderByDate)
            val radioButtonOrderByTitle: RadioButton = dialogView.findViewById(R.id.orderByTitle)
            val radioButtonOrderByPrice: RadioButton = dialogView.findViewById(R.id.orderByPrice)
            val radioButtonOrderByPopularity: RadioButton =
                dialogView.findViewById(R.id.orderByPopularity)
            val radioButtonOrderByRating: RadioButton = dialogView.findViewById(R.id.orderByRating)

            val searchSharedPreferencesEditor = searchSharedPreferences.edit()

            when (searchSharedPreferences.getInt(ID_FILTER_SHARED_PREFERENCES, 0).toInt()) {
                1 -> {
                    radioButtonOrderByDate.isChecked = true
                }
                2 -> {
                    radioButtonOrderByTitle.isChecked = true
                }
                3 -> {
                    radioButtonOrderByPrice.isChecked = true
                }
                4 -> {
                    radioButtonOrderByPopularity.isChecked = true
                }
                5 -> {
                    radioButtonOrderByRating.isChecked = true
                }
            }

            buttonCheckInternet.setOnClickListener {

                orderby = when (radioGroupOrder.checkedRadioButtonId) {
                    R.id.orderByDate -> {
                        searchSharedPreferencesEditor.putInt(ID_FILTER_SHARED_PREFERENCES, 1)
                        "date"
                    }
                    R.id.orderByTitle -> {
                        searchSharedPreferencesEditor.putInt(ID_FILTER_SHARED_PREFERENCES, 2)
                        "id"
                    }
                    R.id.orderByPrice -> {
                        searchSharedPreferencesEditor.putInt(ID_FILTER_SHARED_PREFERENCES, 3)
                        "title"
                    }
                    R.id.orderByPopularity -> {
                        searchSharedPreferencesEditor.putInt(ID_FILTER_SHARED_PREFERENCES, 4)
                        "price"
                    }
                    R.id.orderByRating -> {
                        searchSharedPreferencesEditor.putInt(ID_FILTER_SHARED_PREFERENCES, 5)
                        "popularity"
                    }
                    else -> {
                        searchSharedPreferencesEditor.putInt(ID_FILTER_SHARED_PREFERENCES, 4)
                        "price"
                    }
                }

                if (search.isNotEmpty()) {
                    searchSharedPreferencesEditor.commit()
                    searchSharedPreferencesEditor.apply()
                    searchProduct(search, orderby)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "لطفا کلمه خود را سرچ کنید",
                        Toast.LENGTH_SHORT
                    ).show()

                    customDialog.dismiss()
                }

                customDialog.dismiss()
            }
        }
    }

    private fun searchView() {
        searchBinding.searchViewSearchProduct.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                search = query.toString()
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
                    when (it) {
                        is ResultWrapper.Loading -> {
                            searchBinding.lottieAnimationViewErrorSearch.visibility =
                                View.INVISIBLE
                            searchBinding.lottieAnimationViewLoadingSearch.visibility =
                                View.VISIBLE
                            searchBinding.textViewErrorLoadingSearch.text =
                                "در حال بارگذاری"
                            searchBinding.cardViewSearchCheckingSearch.visibility = View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            searchBinding.cardViewSearchCheckingSearch.visibility = View.GONE
                            searchBinding.lottieAnimationViewErrorSearch.visibility =
                                View.GONE
                            searchBinding.lottieAnimationViewLoadingSearch.visibility =
                                View.GONE
                            searchBinding.textViewErrorLoadingSearch.text = ""
                            searchAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            searchBinding.lottieAnimationViewErrorSearch.visibility =
                                View.VISIBLE
                            searchBinding.lottieAnimationViewLoadingSearch.visibility =
                                View.INVISIBLE
                            searchBinding.textViewErrorLoadingSearch.text =
                                "خطا در بارگذاری"
                            searchBinding.cardViewSearchCheckingSearch.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

    }

}