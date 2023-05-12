package com.omidrezabagherian.totishop.ui.subcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.subcategory.SubCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {

    private val _subCategoryList:MutableStateFlow<ResultWrapper<List<SubCategory>>> = MutableStateFlow(ResultWrapper.Loading)
    val subCategoryList: StateFlow<ResultWrapper<List<SubCategory>>> = _subCategoryList

    fun getSubCategoryList(parent: Int) {
        viewModelScope.launch {
            val responseCategoryList = showRepository.getSubCategoryList(parent)
            responseCategoryList.collect {
                _subCategoryList.emit(it)
            }
        }
    }

}