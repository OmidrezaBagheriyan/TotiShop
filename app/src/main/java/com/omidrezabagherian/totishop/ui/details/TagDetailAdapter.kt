package com.omidrezabagherian.totishop.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omidrezabagherian.totishop.databinding.ItemDetailTagBinding
import com.omidrezabagherian.totishop.domain.model.product.Tag

class TagDetailAdapter(private val details: (Tag) -> Unit) :
    ListAdapter<Tag, TagDetailAdapter.TagViewHolder>(TagDetailDiffCall()) {

    class TagViewHolder(
        private val itemDetailTagBinding: ItemDetailTagBinding,
        private val details: (Tag) -> Unit
    ) : RecyclerView.ViewHolder(itemDetailTagBinding.root) {
        fun bind(tag: Tag) {
            itemDetailTagBinding.root.setOnClickListener {
                details(tag)
            }
            itemDetailTagBinding.textViewDetailTagName.text = tag.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(
            ItemDetailTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), details
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}