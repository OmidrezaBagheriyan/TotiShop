package com.omidrezabagherian.totishop.ui.listproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListProductViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _productProductList = MutableStateFlow<List<Product>>(emptyList())
    val productProductList: StateFlow<List<Product>> = _productProductList

    private val _productError = MutableStateFlow(false)
    val productError: StateFlow<Boolean> = _productError

    fun getProductList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 50, filter)
            withContext(Dispatchers.Main) {
                if (responseProductList.isSuccessful) {
                    _productProductList.emit(responseProductList.body()!!)
                } else {
                    _productError.emit(true)
                }
            }
        }
    }

}