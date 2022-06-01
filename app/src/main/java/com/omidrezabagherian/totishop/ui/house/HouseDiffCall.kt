package com.omidrezabagherian.totishop.ui.house

import androidx.recyclerview.widget.DiffUtil
import com.omidrezabagherian.totishop.domain.model.product.Product

class HouseDiffCall : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}