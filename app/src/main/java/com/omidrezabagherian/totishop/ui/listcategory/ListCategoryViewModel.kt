package com.omidrezabagherian.totishop.ui.listcategory

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
class ListCategoryViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _productCategoryList = MutableStateFlow<List<Product>>(emptyList())
    val productCategoryList: StateFlow<List<Product>> = _productCategoryList

    private val _productError = MutableStateFlow(false)
    val productError: StateFlow<Boolean> = _productError

    fun getProductSubCategoryList(category: Int) {
        viewModelScope.launch {
            val responseProductCategoryList =
                showRepository.getProductSubCategoryList(1, 50, category)
            withContext(Dispatchers.Main) {
                if (responseProductCategoryList.isSuccessful) {
                    _productCategoryList.emit(responseProductCategoryList.body()!!)
                } else {
                    _productError.emit(true)
                }
            }
        }
    }

}