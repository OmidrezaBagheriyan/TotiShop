package com.omidrezabagherian.totishop.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _productSearchList =
        MutableStateFlow<ResultWrapper<List<Product>>>(ResultWrapper.Loading)
    val productSearchList: StateFlow<ResultWrapper<List<Product>>> =
        _productSearchList.asStateFlow()

    fun getProductCategoryList(search: String, orderby: String) {
        viewModelScope.launch {
            val responseProductSearchList =
                shopRepository.getProductSearchList(1, 20, search, orderby)
            withContext(Dispatchers.Main) {
                responseProductSearchList.collect {
                    _productSearchList.emit(it)
                }
            }
        }
    }

}