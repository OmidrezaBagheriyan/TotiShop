package com.omidrezabagherian.totishop.ui.subcategory

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.core.ResultWrapper
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
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                        }
                        is ResultWrapper.Success -> {
                            subCategoryAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}