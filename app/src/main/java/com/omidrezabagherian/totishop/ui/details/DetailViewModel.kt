package com.omidrezabagherian.totishop.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.product.Product
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
class DetailViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _productValues = MutableSharedFlow<Product>()
    val productValues: SharedFlow<Product> = _productValues

    fun getProduct(id: Int) {
        viewModelScope.launch {
            val responseProduct = shopRepository.getProduct(id)
            withContext(Dispatchers.Main) {
                if (responseProduct.isSuccessful) {
                    _productValues.emit(responseProduct.body()!!)
                }
            }
        }
    }


}