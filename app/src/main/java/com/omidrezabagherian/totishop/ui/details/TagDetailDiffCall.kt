package com.omidrezabagherian.totishop.ui.details

import androidx.recyclerview.widget.DiffUtil
import com.omidrezabagherian.totishop.domain.model.product.Tag

class TagDetailDiffCall : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }
}