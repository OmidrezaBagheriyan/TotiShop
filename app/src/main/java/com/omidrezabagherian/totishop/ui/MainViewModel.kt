package com.omidrezabagherian.totishop.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.customer.Customer
import com.omidrezabagherian.totishop.domain.model.order.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _customer = MutableStateFlow<ResultWrapper<Customer>>(ResultWrapper.Loading)
    val customer: StateFlow<ResultWrapper<Customer>> = _customer.asStateFlow()

    private val _setProductBagList = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val setProductBagList: StateFlow<ResultWrapper<Order>> = _setProductBagList

    init {
        viewModelScope.launch {
            delay(5000)
            _isLoading.value = false
        }
    }


    fun setOrders(createOrder: CreateOrder) {
        viewModelScope.launch {
            val responseProductBagList = shopRepository.setOrders(createOrder)
            responseProductBagList.collect {
                _setProductBagList.emit(it)
            }
        }
    }

    fun getCustomer(id: Int) {
        viewModelScope.launch {
            val responseCustomer = shopRepository.getCustomer(id)
            responseCustomer.collect {
                _customer.emit(it)
            }
        }
    }

}