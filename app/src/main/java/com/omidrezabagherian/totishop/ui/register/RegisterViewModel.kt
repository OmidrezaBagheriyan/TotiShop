package com.omidrezabagherian.totishop.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.customer.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _addCustomerInfo = MutableStateFlow<ResultWrapper<Customer>>(ResultWrapper.Loading)
    val addCustomerInfo: StateFlow<ResultWrapper<Customer>> = _addCustomerInfo.asStateFlow()

    fun setCustomer(createCustomer: CreateCustomer) {
        viewModelScope.launch {
            val responseAddCustomer = showRepository.setCustomer(createCustomer)
            withContext(Dispatchers.Main) {
                responseAddCustomer.collect {
                    _addCustomerInfo.emit(it)
                }

            }
        }
    }

}