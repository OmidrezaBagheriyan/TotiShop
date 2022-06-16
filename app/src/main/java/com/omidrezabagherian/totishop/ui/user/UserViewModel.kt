package com.omidrezabagherian.totishop.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
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
class UserViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _customer = MutableSharedFlow<Customer>()
    val customer: SharedFlow<Customer> = _customer

    private val _customerError = MutableStateFlow(false)
    val customerError: StateFlow<Boolean> = _customerError

    fun getCustomer(id: Int) {
        viewModelScope.launch {
            val responseCustomer = showRepository.getCustomer(id)
            withContext(Dispatchers.Main) {
                if (responseCustomer.isSuccessful) {
                    _customer.emit(responseCustomer.body()!!)
                } else {
                    _customerError.emit(true)
                }
            }
        }
    }

}