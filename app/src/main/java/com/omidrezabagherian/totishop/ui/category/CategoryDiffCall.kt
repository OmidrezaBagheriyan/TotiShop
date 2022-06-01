package com.omidrezabagherian.totishop.ui.category

import androidx.recyclerview.widget.DiffUtil
import com.omidrezabagherian.totishop.domain.model.category.Category

class CategoryDiffCall:DiffUtil.ItemCallback<Category>(){
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}