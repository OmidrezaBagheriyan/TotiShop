package com.omidrezabagherian.totishop.ui.details

import androidx.recyclerview.widget.DiffUtil
import com.omidrezabagherian.totishop.domain.model.product.Category

class CategoryDetailDiffCall : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}