package com.omidrezabagherian.totishop.ui.listcategory

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.util.NetworkManager
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.databinding.FragmentListCategoryBinding
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
                listCategoryBinding.textViewListCategory.text = listCategoryArgs.name
                productPopularityList()
            } else {
                dialogCheckInternet()
            }
        }
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

        listCategoryViewModel.getProductSubCategoryList(listCategoryArgs.id)

        listCategoryBinding.recyclerViewListCategory.adapter = listCategoryAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                listCategoryViewModel.productCategoryList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            listCategoryBinding.recyclerViewListCategory.visibility = View.GONE
                            listCategoryBinding.lottieAnimationViewErrorListCategory.visibility =
                                View.INVISIBLE
                            listCategoryBinding.lottieAnimationViewLoadingListCategory.visibility =
                                View.VISIBLE
                            listCategoryBinding.textViewErrorLoadingListCategory.text =
                                "در حال بارگذاری"
                            listCategoryBinding.cardViewListCategoryCheckingListCategory.visibility =
                                View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            listCategoryBinding.cardViewListCategoryCheckingListCategory.visibility =
                                View.GONE
                            listCategoryBinding.lottieAnimationViewErrorListCategory.visibility =
                                View.GONE
                            listCategoryBinding.lottieAnimationViewLoadingListCategory.visibility =
                                View.GONE
                            listCategoryBinding.textViewErrorLoadingListCategory.text = ""

                            listCategoryBinding.recyclerViewListCategory.visibility = View.VISIBLE

                            listCategoryAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            listCategoryBinding.recyclerViewListCategory.visibility = View.GONE
                            listCategoryBinding.lottieAnimationViewErrorListCategory.visibility =
                                View.VISIBLE
                            listCategoryBinding.lottieAnimationViewLoadingListCategory.visibility =
                                View.INVISIBLE
                            listCategoryBinding.textViewErrorLoadingListCategory.text =
                                "خطا در بارگذاری"
                            listCategoryBinding.cardViewListCategoryCheckingListCategory.visibility =
                                View.VISIBLE
                        }
                    }
                }
            }
        }
    }

}