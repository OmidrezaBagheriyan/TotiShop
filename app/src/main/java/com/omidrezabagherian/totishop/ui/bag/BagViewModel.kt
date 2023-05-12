package com.omidrezabagherian.totishop.ui.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.coupons.Coupon
import com.omidrezabagherian.totishop.domain.model.order.Order
import com.omidrezabagherian.totishop.domain.model.updateorder.UpdateOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BagViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _getProductBagList = MutableStateFlow<ResultWrapper<Order>>(ResultWrapper.Loading)
    val getProductBagList: StateFlow<ResultWrapper<Order>> = _getProductBagList.asStateFlow()

    private val _getCouponsList = MutableStateFlow<ResultWrapper<List<Coupon>>>(ResultWrapper.Loading)
    val getCouponsList: StateFlow<ResultWrapper<List<Coupon>>> = _getCouponsList.asStateFlow()

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

    fun getCoupons(search:String) {
        viewModelScope.launch {
            val responseGetCoupons = shopRepository.getCoupons(search)
            responseGetCoupons.collect{
                _getCouponsList.emit(it)
            }
        }
    }



}