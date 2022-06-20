package com.omidrezabagherian.totishop.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.customer.Customer
import com.omidrezabagherian.totishop.domain.model.order.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _customer = MutableStateFlow<ResultWrapper<Customer>>(ResultWrapper.Loading)
    val customer: StateFlow<ResultWrapper<Customer>> = _customer.asStateFlow()

    private val _setProductBagList = MutableSharedFlow<Order>()
    val setProductBagList: SharedFlow<Order> = _setProductBagList

    private val _customerError = MutableStateFlow(false)
    val customerError: StateFlow<Boolean> = _customerError

    init {
        viewModelScope.launch {
            delay(5000)
            _isLoading.value = false
        }
    }


    fun setOrders(createOrder: CreateOrder) {
        viewModelScope.launch {
            val responseProductBagList = shopRepository.setOrders(createOrder)
            withContext(Dispatchers.Main) {
                if (responseProductBagList.isSuccessful) {
                    _setProductBagList.emit(responseProductBagList.body()!!)
                } else {
                    _customerError.emit(true)
                }
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