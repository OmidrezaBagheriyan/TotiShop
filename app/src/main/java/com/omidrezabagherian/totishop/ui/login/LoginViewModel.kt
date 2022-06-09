package com.omidrezabagherian.totishop.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.category.Category
import com.omidrezabagherian.totishop.domain.model.customer.Customer
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
class LoginViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _customerInfo = MutableSharedFlow<List<Customer>>()
    val customerInfo: SharedFlow<List<Customer>> = _customerInfo

    private val _customerError = MutableStateFlow(false)
    val customerError: StateFlow<Boolean> = _customerError

    fun getCustomer(email: String) {
        viewModelScope.launch {
            val responseCustomer = showRepository.getCustomer(email)
            withContext(Dispatchers.Main) {
                if (responseCustomer.isSuccessful) {
                    _customerInfo.emit(responseCustomer.body()!!)
                } else {
                    _customerError.emit(true)
                }
            }
        }
    }

}