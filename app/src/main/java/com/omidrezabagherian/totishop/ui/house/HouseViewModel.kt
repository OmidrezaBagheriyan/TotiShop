package com.omidrezabagherian.totishop.ui.house

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.data.remote.ShopService
import com.omidrezabagherian.totishop.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseViewModel @Inject constructor(private val shopRepository: ShopRepository) : ViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList = _productList.asStateFlow()

    private val _productError = MutableStateFlow(false)
    val productError = _productError.asStateFlow()

    fun getProductList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 1, filter)
            if (responseProductList.isSuccessful) {
                _productList.value = responseProductList.body()!!
            } else {
                _productError.value = true
            }
        }
    }

}