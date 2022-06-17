package com.omidrezabagherian.totishop.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.order.Order
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _productValues = MutableSharedFlow<Product>()
    val productValues: SharedFlow<Product> = _productValues

    private val _getProductBagList = MutableSharedFlow<Order>()
    val getProductBagList: SharedFlow<Order> = _getProductBagList

    private val _putProductBagList = MutableSharedFlow<Order>()
    val putProductBagList: SharedFlow<Order> = _putProductBagList

    private val _productError = MutableStateFlow(false)
    val productError: StateFlow<Boolean> = _productError

    fun getProduct(id: Int) {
        viewModelScope.launch {
            val responseProduct = shopRepository.getProduct(id)
            withContext(Dispatchers.Main) {
                if (responseProduct.isSuccessful) {
                    _productValues.emit(responseProduct.body()!!)
                } else {
                    _productError.emit(true)
                }
            }
        }
    }

    fun getOrders(id: Int) {
        viewModelScope.launch {
            val responseProductBagList = shopRepository.getOrders(id)
            withContext(Dispatchers.Main) {
                if (responseProductBagList.isSuccessful) {
                    _getProductBagList.emit(responseProductBagList.body()!!)
                } else {
                    _productError.emit(true)
                }
            }
        }
    }

    fun putOrders(id: Int, createOrder: CreateOrder) {
        viewModelScope.launch {
            val responseProductBagList = shopRepository.addProductToOrders(id, createOrder)
            if (responseProductBagList.isSuccessful) {
                _getProductBagList.emit(responseProductBagList.body()!!)
            } else {
                _productError.emit(true)
            }
        }
    }
}
