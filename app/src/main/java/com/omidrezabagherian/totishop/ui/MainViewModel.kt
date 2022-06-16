package com.omidrezabagherian.totishop.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.customer.Customer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _customer = MutableSharedFlow<Customer>()
    val customer: SharedFlow<Customer> = _customer

    private val _customerError = MutableStateFlow(false)
    val customerError: StateFlow<Boolean> = _customerError

    init {
        viewModelScope.launch {
            delay(5000)
            _isLoading.value = false
        }
    }

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