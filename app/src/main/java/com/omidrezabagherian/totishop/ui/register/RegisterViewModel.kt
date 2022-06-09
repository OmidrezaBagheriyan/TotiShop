package com.omidrezabagherian.totishop.ui.register

import androidx.lifecycle.ViewModel
import com.omidrezabagherian.totishop.data.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val showRepository: ShopRepository) :
    ViewModel() {



}