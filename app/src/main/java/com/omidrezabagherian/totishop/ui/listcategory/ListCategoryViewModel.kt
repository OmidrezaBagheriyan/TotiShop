package com.omidrezabagherian.totishop.ui.listcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListCategoryViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _productCategoryList: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productCategoryList: StateFlow<ResultWrapper<List<Product>>> = _productCategoryList

    fun getProductSubCategoryList(category: Int) {
        viewModelScope.launch {
            val responseProductCategoryList =
                showRepository.getProductSubCategoryList(1, 50, category)
            responseProductCategoryList.collect {
                _productCategoryList.emit(it)
            }
        }
    }

}