package com.omidrezabagherian.totishop.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omidrezabagherian.totishop.core.ResultWrapper
import com.omidrezabagherian.totishop.data.ShopRepository
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

    fun getReviews(product: Int) {
        viewModelScope.launch {
            val responseReviewList = shopRepository.getReviews(product, 1, 30)
            responseReviewList.collect {
                _getReviews.emit(it)
            }
        }
    }
}