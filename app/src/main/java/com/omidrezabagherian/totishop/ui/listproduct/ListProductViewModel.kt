package com.omidrezabagherian.totishop.ui.listproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListProductViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _productList: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productList: StateFlow<ResultWrapper<List<Product>>> = _productList.asStateFlow()

    private val _productError = MutableStateFlow(false)
    val productError: StateFlow<Boolean> = _productError

    fun getProductList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 50, filter)
            withContext(Dispatchers.Main) {
                responseProductList.collect {
                    _productList.emit(it)
                }
            }
        }
    }

}