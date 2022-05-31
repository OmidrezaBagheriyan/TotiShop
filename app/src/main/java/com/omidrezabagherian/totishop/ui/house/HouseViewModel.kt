package com.omidrezabagherian.totishop.ui.house

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.data.remote.ShopService
import com.omidrezabagherian.totishop.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HouseViewModel @Inject constructor(private val shopRepository: ShopRepository) : ViewModel() {

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> = _productList

    private val _productError = MutableLiveData(false)
    val productError: LiveData<Boolean> = _productError

    fun getProductList(filter: Map<String, String>) {
        viewModelScope.launch {
            val responseProductList = shopRepository.getProductList(1, 1, filter)
            withContext(Dispatchers.Main) {
                if (responseProductList.isSuccessful) {
                    _productList.postValue(responseProductList.body()!!)
                } else {
                    _productError.postValue(true)
                }
            }
        }
    }

}