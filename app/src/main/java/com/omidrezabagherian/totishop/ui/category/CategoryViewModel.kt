package com.omidrezabagherian.totishop.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.category.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _categoryList: MutableStateFlow<ResultWrapper<List<Category>>> =
        MutableStateFlow(ResultWrapper.Loading)
    val categoryList: StateFlow<ResultWrapper<List<Category>>> = _categoryList

    fun getCategoryList() {
        viewModelScope.launch {
            val responseCategoryList = showRepository.getCategoryList(1, 20)
            responseCategoryList.collect {
                _categoryList.emit(it)
            }
        }
    }
}