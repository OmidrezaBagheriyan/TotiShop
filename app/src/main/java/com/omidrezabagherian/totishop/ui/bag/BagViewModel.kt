package com.omidrezabagherian.totishop.ui.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.createorder.CreateOrder
import com.omidrezabagherian.totishop.domain.model.order.Order
import com.omidrezabagherian.totishop.domain.model.updateorder.UpdateOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BagViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _getProductBagList = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val getProductBagList: StateFlow<ResultWrapper<Order>> = _getProductBagList.asStateFlow()

    private val _putProductBagList =  MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val putProductBagList: StateFlow<ResultWrapper<Order>> = _putProductBagList.asStateFlow()

    fun getOrders(id: Int) {
        viewModelScope.launch {
            val responseProductBagList = shopRepository.getOrders(id)
            responseProductBagList.collect {
                _getProductBagList.emit(it)
            }
        }
    }

    fun editQuantityToOrders(id: Int, updateOrder: UpdateOrder) {
        viewModelScope.launch {
            val responseProductBagList = shopRepository.editQuantityToOrders(id, updateOrder)
            responseProductBagList.collect {
                _putProductBagList.emit(it)
            }
        }
    }

}