package com.omidrezabagherian.totishop.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.order.Order
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _productValues = MutableStateFlow<ResultWrapper<Product>>(ResultWrapper.Loading)
    val productValues: StateFlow<ResultWrapper<Product>> = _productValues.asStateFlow()

    private val _getProductBagList = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val getProductBagList: StateFlow<ResultWrapper<Order>> = _getProductBagList.asStateFlow()

    private val _putProductBagList = MutableSharedFlow<Order>()
    val putProductBagList: SharedFlow<Order> = _putProductBagList

    private val _productError = MutableStateFlow(false)
    val productError: StateFlow<Boolean> = _productError

    fun getProduct(id: Int) {
        viewModelScope.launch {
            val responseProduct = shopRepository.getProduct(id)
            withContext(Dispatchers.Main) {
                responseProduct.collect {
                    _productValues.emit(it)
                }
            }
        }
    }

    fun getOrders(id: Int) {
        viewModelScope.launch {
            val responseProductBagList = shopRepository.getOrders(id)
            responseProductBagList.collect {
                _getProductBagList.emit(it)
            }
        }
    }

    fun putOrders(id: Int, createOrder: CreateOrder) {
        viewModelScope.launch {
            val responseProductBagList = shopRepository.addProductToOrders(id, createOrder)
            //Todo درسنش کن
            /*if (responseProductBagList.isSuccessful) {
                _getProductBagList.emit(responseProductBagList.body()!!)
            } else {
                _productError.emit(true)
            }*/
        }
    }
}
