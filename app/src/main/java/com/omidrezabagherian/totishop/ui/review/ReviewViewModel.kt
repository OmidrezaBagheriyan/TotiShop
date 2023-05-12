package com.omidrezabagherian.totishop.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.util.ResultWrapper
import com.omidrezabagherian.totishop.domain.repositories.ShopRepository
import com.omidrezabagherian.totishop.domain.model.deletereview.DeleteReview
import com.omidrezabagherian.totishop.domain.model.review.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    ViewModel() {

    private val _getReviews = MutableStateFlow<ResultWrapper<List<Review>>>(ResultWrapper.Loading)
    val getReviews: StateFlow<ResultWrapper<List<Review>>> = _getReviews.asStateFlow()

    private val _deleteReviews =
        MutableStateFlow<ResultWrapper<DeleteReview>>(ResultWrapper.Loading)
    val deleteReviews: StateFlow<ResultWrapper<DeleteReview>> = _deleteReviews.asStateFlow()


    fun getReviews(product: Int) {
        viewModelScope.launch {
            val responseReviewList = shopRepository.getReviews(product, 1, 30)
            responseReviewList.collect {
                _getReviews.emit(it)
            }
        }
    }

    fun deleteReviews(review: Int, force: Boolean) {
        viewModelScope.launch {
            val responseReviewList = shopRepository.deleteReviews(review, force)
            responseReviewList.collect {
                _deleteReviews.emit(it)
            }
        }
    }
}