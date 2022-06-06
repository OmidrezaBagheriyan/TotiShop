package com.omidrezabagherian.totishop.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _productSearchList = MutableStateFlow<List<Product>>(emptyList())
    val productSearchList: StateFlow<List<Product>> = _productSearchList

    private val _productError = MutableStateFlow(false)
    val productError: StateFlow<Boolean> = _productError

    fun getProductCategoryList(search: String) {
        viewModelScope.launch {
            val responseProductSearchList = showRepository.getProductSearchList(1, 20, search)
            withContext(Dispatchers.Main) {
                if (responseProductSearchList.isSuccessful) {
                    _productSearchList.emit(responseProductSearchList.body()!!)
                } else {
                    _productError.emit(true)
                }
            }
        }
    }

}