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
import com.omidrezabagherian.totishop.core.NetworkManager
import com.omidrezabagherian.totishop.databinding.FragmentHouseBinding
import com.omidrezabagherian.totishop.databinding.FragmentListProductBinding
import com.omidrezabagherian.totishop.ui.house.HouseAdapter
import com.omidrezabagherian.totishop.ui.house.HouseFragmentDirections
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
                listProductViewModel.productProductList.collect {
                    listProductAdapter.submitList(it)
                }
            }
        }

    }

}