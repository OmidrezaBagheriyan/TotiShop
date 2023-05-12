package com.omidrezabagherian.totishop.ui.subcategory

import android.app.AlertDialog
import android.content.res.Configuration
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
import androidx.recyclerview.widget.GridLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.util.NetworkManager
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.databinding.FragmentSubCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubCategoryFragment : Fragment(R.layout.fragment_sub_category) {
    private lateinit var subCategoryBinding: FragmentSubCategoryBinding
    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    private val subCategoryFragmentArgs: SubCategoryFragmentArgs by navArgs()
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subCategoryBinding = FragmentSubCategoryBinding.bind(view)

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
                checkSubCategory()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun checkSubCategory() {
        val subCategoryAdapter = SubCategoryAdapter(details = { subCategory ->
            navController.navigate(
                SubCategoryFragmentDirections.actionSubCategoryFragmentToListCategoryFragment(
                    subCategory.name, subCategory.id
                )
            )
        })

        subCategoryBinding.materialButtonSubCategoryShowMore.setOnClickListener {
            navController.navigate(
                SubCategoryFragmentDirections.actionSubCategoryFragmentToListCategoryFragment(
                    subCategoryFragmentArgs.name, subCategoryFragmentArgs.id
                )
            )
        }

        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            subCategoryBinding.recyclerViewSubCategory.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        } else {
            subCategoryBinding.recyclerViewSubCategory.layoutManager =
                GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
        }

        subCategoryViewModel.getSubCategoryList(subCategoryFragmentArgs.id)

        subCategoryBinding.recyclerViewSubCategory.adapter = subCategoryAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                subCategoryViewModel.subCategoryList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            subCategoryBinding.materialButtonSubCategoryShowMore.visibility =
                                View.GONE
                            subCategoryBinding.recyclerViewSubCategory.visibility = View.GONE
                            subCategoryBinding.lottieAnimationViewErrorSubCategory.visibility =
                                View.INVISIBLE
                            subCategoryBinding.lottieAnimationViewLoadingSubCategory.visibility =
                                View.VISIBLE
                            subCategoryBinding.textViewErrorLoadingSubCategory.text =
                                "در حال بارگذاری"
                            subCategoryBinding.cardViewSubCategoryCheckingSubCategory.visibility =
                                View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            subCategoryBinding.materialButtonSubCategoryShowMore.visibility =
                                View.VISIBLE
                            subCategoryBinding.cardViewSubCategoryCheckingSubCategory.visibility =
                                View.GONE
                            subCategoryBinding.lottieAnimationViewErrorSubCategory.visibility =
                                View.GONE
                            subCategoryBinding.lottieAnimationViewLoadingSubCategory.visibility =
                                View.GONE
                            subCategoryBinding.textViewErrorLoadingSubCategory.text = ""

                            subCategoryBinding.recyclerViewSubCategory.visibility = View.VISIBLE

                            subCategoryAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            subCategoryBinding.materialButtonSubCategoryShowMore.visibility =
                                View.GONE
                            subCategoryBinding.recyclerViewSubCategory.visibility = View.GONE
                            subCategoryBinding.lottieAnimationViewErrorSubCategory.visibility =
                                View.VISIBLE
                            subCategoryBinding.lottieAnimationViewLoadingSubCategory.visibility =
                                View.INVISIBLE
                            subCategoryBinding.textViewErrorLoadingSubCategory.text =
                                "خطا در بارگذاری"
                            subCategoryBinding.cardViewSubCategoryCheckingSubCategory.visibility =
                                View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}