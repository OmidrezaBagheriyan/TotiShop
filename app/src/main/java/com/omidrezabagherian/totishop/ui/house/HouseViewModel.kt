package com.omidrezabagherian.totishop.ui.house

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HouseViewModel @Inject constructor(private val shopRepository: ShopRepository) : ViewModel() {

    private val _productDateList = MutableStateFlow<List<Product>>(emptyList())
    val productDateList: StateFlow<List<Product>> = _productDateList

    private val _productPopularityList = MutableStateFlow<List<Product>>(emptyList())
    val productPopularityList: StateFlow<List<Product>> = _productPopularityList

    private val _productRatingList = MutableStateFlow<List<Product>>(emptyList())
    val productRatingList: StateFlow<List<Product>> = _productRatingList

    private val _sliderImage = MutableSharedFlow<Product>()
    val sliderImage: SharedFlow<Product> = _sliderImage

    private val _productError = MutableStateFlow(false)
    val productError: StateFlow<Boolean> = _productError

    fun getProductDateList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 10, filter)
            withContext(Dispatchers.Main) {
                if (responseProductList.isSuccessful) {
                    _productDateList.emit(responseProductList.body()!!)
                } else {
                    _productError.emit(true)
                }
            }
        }
    }

    fun getProductPopularityList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 10, filter)
            withContext(Dispatchers.Main) {
                if (responseProductList.isSuccessful) {
                    _productPopularityList.emit(responseProductList.body()!!)
                } else {
                    _productError.emit(true)
                }
            }
        }
    }

    fun getProductRatingList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 10, filter)
            withContext(Dispatchers.Main) {
                if (responseProductList.isSuccessful) {
                    _productRatingList.emit(responseProductList.body()!!)
                } else {
                    _productError.emit(true)
                }
            }
        }
    }

    fun getSliderImageList(id: Int) {
        viewModelScope.launch {
            val responseProduct = shopRepository.getProduct(id)
            withContext(Dispatchers.Main) {
                if (responseProduct.isSuccessful) {
                    _sliderImage.emit(responseProduct.body()!!)
                }
            }
        }
    }

}