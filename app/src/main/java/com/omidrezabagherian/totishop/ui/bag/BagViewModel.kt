package com.omidrezabagherian.totishop.ui.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.order.Order
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
class BagViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _setProductBagList = MutableSharedFlow<Order>()
    val setProductBagList: SharedFlow<Order> = _setProductBagList

    private val _getProductBagList = MutableSharedFlow<Order>()
    val getProductBagList: SharedFlow<Order> = _getProductBagList

    private val _orderError = MutableStateFlow(false)
    val orderError: StateFlow<Boolean> = _orderError

    fun setOrders(createOrder: CreateOrder) {
        viewModelScope.launch {
            val responseProductBagList = showRepository.setOrders(createOrder)
            withContext(Dispatchers.Main) {
                if (responseProductBagList.isSuccessful) {
                    _setProductBagList.emit(responseProductBagList.body()!!)
                } else {
                    _orderError.emit(true)
                }
            }
        }
    }

    fun getOrders(id: Int) {
        viewModelScope.launch {
            val responseProductBagList = showRepository.getOrders(id)
            withContext(Dispatchers.Main) {
                if (responseProductBagList.isSuccessful) {
                    _getProductBagList.emit(responseProductBagList.body()!!)
                } else {
                    _orderError.emit(true)
                }
            }
        }
    }

}