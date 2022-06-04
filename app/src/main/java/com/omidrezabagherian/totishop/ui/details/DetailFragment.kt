package com.omidrezabagherian.totishop.ui.details

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_details) {

    private lateinit var detailsBinding: FragmentDetailsBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private val detailArgs: DetailFragmentArgs by navArgs()
    private val navController by lazy {
        findNavController()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsBinding = FragmentDetailsBinding.bind(view)

        checkInternet()

    }

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkInternet() {
        val networkConnection = NetworkManager(requireContext())
        networkConnection.observe(viewLifecycleOwner) { isConnect ->
            if (isConnect) {
                detailViewModel.getProduct(detailArgs.id)

                showDetails()
            } else {
                dialogCheckInternet()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDetails() {
        val categoryAdapter = CategoryDetailAdapter(details = { category ->
            navController.navigate(
                DetailFragmentDirections.actionDetailFragmentToListCategoryFragment(
                    category.name,
                    category.id
                )
            )
        })

        val tagAdapter = TagDetailAdapter(details = { tag ->
            Toast.makeText(requireContext(), tag.name, Toast.LENGTH_SHORT).show()
        })

        detailsBinding.recyclerViewDetailCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        detailsBinding.recyclerViewDetailTags.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.productValues.collect { product ->
                    val imageAdapter = ImageAdapter(requireContext(), product.images)
                    detailsBinding.viewPagerDetailsImages.adapter = imageAdapter
                    detailsBinding.textViewDetailName.text = product.name
                    detailsBinding.textViewDetailNumberRate.text =
                        " ${product.rating_count} امتیاز /"
                    detailsBinding.ratingDetailNumberRate.rating = product.rating_count.toFloat()
                    categoryAdapter.submitList(product.categories)
                    detailsBinding.recyclerViewDetailCategories.adapter = categoryAdapter
                    tagAdapter.submitList(product.tags)
                    detailsBinding.recyclerViewDetailTags.adapter = tagAdapter
                    detailsBinding.textViewDetailPrice.text = "${product.price} تومان"
                    detailsBinding.textViewDetailDescription.text =
                        Html.fromHtml(product.description, 0)
                }
            }
        }
    }

}