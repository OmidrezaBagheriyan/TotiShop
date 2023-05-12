package com.omidrezabagherian.totishop.ui.listproduct

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
import com.omidrezabagherian.totishop.databinding.FragmentListProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.HashMap

@AndroidEntryPoint
class ListProductFragment : Fragment(R.layout.fragment_list_product) {

    private lateinit var listProductBinding: FragmentListProductBinding
    private val listProductViewModel: ListProductViewModel by viewModels()
    private val listProductFragmentArgs: ListProductFragmentArgs by navArgs()
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listProductBinding = FragmentListProductBinding.bind(view)

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
                initProduct()
            } else {
                dialogCheckInternet()
            }
        }
    }

    private fun initProduct() {
        val word = listProductFragmentArgs.valueProduct

        listProductBinding.textViewListProductTitle.text = listProductFragmentArgs.nameProduct

        val productsDateMap = HashMap<String, String>().apply {
            put("orderby", word)
        }
        productDateList(productsDateMap)
    }

    private fun productDateList(productsMap: HashMap<String, String>) {
        val listProductAdapter = ListProductAdapter(details = { product ->
            navController.navigate(
                ListProductFragmentDirections.actionListProductFragmentToDetailFragment(
                    product.id
                )
            )
        })

        listProductBinding.recyclerViewListProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        listProductViewModel.getProductList(productsMap)

        listProductBinding.recyclerViewListProduct.adapter = listProductAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                listProductViewModel.productList.collect {
                    when (it) {
                        is ResultWrapper.Loading -> {
                            listProductBinding.recyclerViewListProduct.visibility = View.GONE
                            listProductBinding.lottieAnimationViewErrorListProduct.visibility =
                                View.INVISIBLE
                            listProductBinding.lottieAnimationViewLoadingListProduct.visibility =
                                View.VISIBLE
                            listProductBinding.textViewErrorLoadingListProduct.text =
                                "در حال بارگذاری"
                            listProductBinding.cardViewListProductCheckingListProduct.visibility = View.VISIBLE
                        }
                        is ResultWrapper.Success -> {
                            listProductBinding.cardViewListProductCheckingListProduct.visibility = View.GONE
                            listProductBinding.lottieAnimationViewErrorListProduct.visibility =
                                View.GONE
                            listProductBinding.lottieAnimationViewLoadingListProduct.visibility =
                                View.GONE
                            listProductBinding.textViewErrorLoadingListProduct.text = ""

                            listProductBinding.recyclerViewListProduct.visibility = View.VISIBLE

                            listProductAdapter.submitList(it.value)
                        }
                        is ResultWrapper.Error -> {
                            listProductBinding.recyclerViewListProduct.visibility = View.GONE
                            listProductBinding.lottieAnimationViewErrorListProduct.visibility =
                                View.VISIBLE
                            listProductBinding.lottieAnimationViewLoadingListProduct.visibility =
                                View.INVISIBLE
                            listProductBinding.textViewErrorLoadingListProduct.text =
                                "خطا در بارگذاری"
                            listProductBinding.cardViewListProductCheckingListProduct.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

    }

}