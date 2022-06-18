package com.omidrezabagherian.totishop.ui.house

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.core.ResultWrapper
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

    private val _productDateList: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productDateList: StateFlow<ResultWrapper<List<Product>>> = _productDateList.asStateFlow()

    private val _productPopularityList: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productPopularityList: StateFlow<ResultWrapper<List<Product>>> =
        _productPopularityList.asStateFlow()

    private val _productRatingList: MutableStateFlow<ResultWrapper<List<Product>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val productRatingList: StateFlow<ResultWrapper<List<Product>>> =
        _productRatingList.asStateFlow()

    private val _sliderImage = MutableSharedFlow<Product>()
    val sliderImage: SharedFlow<Product> = _sliderImage

    private val _productError = MutableStateFlow(false)
    val productError: StateFlow<Boolean> = _productError

    fun getProductDateList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 10, filter)
            responseProductList.collect {
                _productDateList.emit(it)
            }
        }
    }

    fun getProductPopularityList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 10, filter)
            responseProductList.collect {
                _productPopularityList.emit(it)
            }
        }
    }

    fun getProductRatingList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 10, filter)
            responseProductList.collect {
                _productRatingList.emit(it)
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