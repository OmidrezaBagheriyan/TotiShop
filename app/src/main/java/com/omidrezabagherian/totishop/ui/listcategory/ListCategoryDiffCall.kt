package com.omidrezabagherian.totishop.ui.listcategory

import androidx.recyclerview.widget.DiffUtil
import com.omidrezabagherian.totishop.domain.model.product.Product

class ListCategoryDiffCall : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}