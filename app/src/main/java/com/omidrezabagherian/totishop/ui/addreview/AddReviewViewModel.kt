package com.omidrezabagherian.totishop.ui.addreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.data.ShopRepository
import com.omidrezabagherian.totishop.domain.model.addreview.AddReview
import com.omidrezabagherian.totishop.domain.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReviewViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _setReviews = MutableStateFlow<ResultWrapper<Review>>(ResultWrapper.Loading)
    val setReviews: StateFlow<ResultWrapper<Review>> = _setReviews.asStateFlow()

    fun setReviews(addReview: AddReview) {
        viewModelScope.launch {
            val responseReviewList = shopRepository.setReviews(addReview)
            responseReviewList.collect {
                _setReviews.emit(it)
            }
        }
    }
}