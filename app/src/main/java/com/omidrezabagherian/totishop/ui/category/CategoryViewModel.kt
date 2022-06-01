package com.omidrezabagherian.totishop.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.category.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList: StateFlow<List<Category>> = _categoryList

    private val _categoryError = MutableStateFlow(false)
    val categoryError: StateFlow<Boolean> = _categoryError

    fun getCategoryList() {
        viewModelScope.launch {
            val responseCategoryList = showRepository.getCategoryList(1, 20)
            withContext(Dispatchers.Main) {
                if (responseCategoryList.isSuccessful) {
                    _categoryList.emit(responseCategoryList.body()!!)
                } else {
                    _categoryError.emit(true)
                }
            }
        }
    }
}