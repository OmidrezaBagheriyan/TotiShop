package com.omidrezabagherian.totishop.ui.editreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.editreview.EditReview
import com.omidrezabagherian.totishop.domain.model.getreview.GetReview
import com.omidrezabagherian.totishop.domain.model.putreview.PutReview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditReviewViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _getReview = MutableStateFlow<ResultWrapper<GetReview>>(ResultWrapper.Loading)
    val getReview: StateFlow<ResultWrapper<GetReview>> = _getReview.asStateFlow()

    private val _putReviews = MutableStateFlow<ResultWrapper<PutReview>>(ResultWrapper.Loading)
    val putReviews: StateFlow<ResultWrapper<PutReview>> = _putReviews.asStateFlow()

    fun getReview(id: Int) {
        viewModelScope.launch {
            val responseReviewList = shopRepository.getReview(id)
            responseReviewList.collect {
                _getReview.emit(it)
            }
        }
    }

    fun putReviews(id: Int, addReview: EditReview) {
        viewModelScope.launch {
            val responseReviewList = shopRepository.putReviews(id, addReview)
            responseReviewList.collect {
                _putReviews.emit(it)
            }
        }
    }
}