package com.omidrezabagherian.totishop.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.customer.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _customerInfo =
        MutableStateFlow<ResultWrapper<List<Customer>>>(ResultWrapper.Loading)
    val customerInfo: StateFlow<ResultWrapper<List<Customer>>> = _customerInfo.asStateFlow()

    fun getCustomerByEmail(email: String) {
        viewModelScope.launch {
            val responseCustomer = showRepository.getCustomerByEmail(email)
            responseCustomer.collect {
                _customerInfo.emit(it)
            }
        }
    }

}
