package com.omidrezabagherian.totishop.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.createcustomer.CreateCustomer
import com.omidrezabagherian.totishop.domain.model.customer.Customer
import com.omidrezabagherian.totishop.domain.model.errorcreatecustomer.ErrorCreateCustomer
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
class RegisterViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _addCustomerInfo = MutableSharedFlow<Customer>()
    val addCustomerInfo: SharedFlow<Customer> = _addCustomerInfo

    private val _errorCustomerInfo = MutableStateFlow(false)
    val errorCustomerInfo: StateFlow<Boolean> = _errorCustomerInfo

    private val _addCustomerError = MutableStateFlow(false)
    val addCustomerError: StateFlow<Boolean> = _addCustomerError

    fun setCustomer(createCustomer: CreateCustomer) {
        viewModelScope.launch {
            val responseAddCustomer = showRepository.setCustomer(createCustomer)
            withContext(Dispatchers.Main) {
                if (responseAddCustomer.code() == 201) {
                    if (responseAddCustomer.isSuccessful) {
                        _addCustomerInfo.emit(responseAddCustomer.body()!!)
                    } else {
                        _addCustomerError.emit(true)
                    }
                } else {
                    _errorCustomerInfo.emit(true)
                }
            }
        }
    }

}