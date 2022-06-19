package com.omidrezabagherian.totishop.ui.category

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.ResultWrapper
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

        categoryBinding = FragmentCategoryBinding.bind(view)

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
                checkCategory()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun checkCategory() {
        val categoryAdapter = CategoryAdapter(details = { category ->
            navController.navigate(
                CategoryFragmentDirections.actionCategoryFragmentToSubCategoryFragment(
                    category.id,
                    category.name
                )
            )
        })

        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            categoryBinding.recyclerViewCategory.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        } else {
            categoryBinding.recyclerViewCategory.layoutManager =
                GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        }

        categoryViewModel.getCategoryList()

        categoryBinding.recyclerViewCategory.adapter = categoryAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.categoryList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            categoryBinding.recyclerViewCategory.visibility = View.GONE
                            categoryBinding.lottieAnimationViewErrorCategory.visibility =
                                View.INVISIBLE
                            categoryBinding.lottieAnimationViewLoadingCategory.visibility =
                                View.VISIBLE
                            categoryBinding.textViewErrorLoadingCategory.text =
                                "در حال بارگذاری"
                            categoryBinding.cardViewCategoryCheckingCategory.visibility = View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            categoryBinding.cardViewCategoryCheckingCategory.visibility = View.GONE
                            categoryBinding.lottieAnimationViewErrorCategory.visibility =
                                View.GONE
                            categoryBinding.lottieAnimationViewLoadingCategory.visibility =
                                View.GONE
                            categoryBinding.textViewErrorLoadingCategory.text = ""

                            categoryBinding.recyclerViewCategory.visibility = View.VISIBLE

                            categoryAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            categoryBinding.recyclerViewCategory.visibility = View.GONE
                            categoryBinding.lottieAnimationViewErrorCategory.visibility =
                                View.VISIBLE
                            categoryBinding.lottieAnimationViewLoadingCategory.visibility =
                                View.INVISIBLE
                            categoryBinding.textViewErrorLoadingCategory.text =
                                "خطا در بارگذاری"
                            categoryBinding.cardViewCategoryCheckingCategory.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}