package com.omidrezabagherian.totishop.ui.subcategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.databinding.ItemCategoryBinding
import com.omidrezabagherian.totishop.domain.model.subcategory.SubCategory

class SubCategoryAdapter(private val details: (SubCategory) -> Unit) :
    ListAdapter<SubCategory, SubCategoryAdapter.SubCategoryViewHolder>(SubCategoryDiffCall()) {

    class SubCategoryViewHolder(
        private val itemCategoryBinding: ItemCategoryBinding,
        private val details: (SubCategory) -> Unit
    ) : RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun bind(subCategory: SubCategory) {
            itemCategoryBinding.root.setOnClickListener {
                details(subCategory)
            }

            Glide.with(itemCategoryBinding.root).load(subCategory.image.src).centerInside()
                .into(itemCategoryBinding.imageViewCategoryIcon)

            itemCategoryBinding.textViewCategoryTitle.text = subCategory.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubCategoryViewHolder {
        return SubCategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            details
        )
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class SubCategoryDiffCall : DiffUtil.ItemCallback<SubCategory>() {
    override fun areItemsTheSame(oldItem: SubCategory, newItem: SubCategory): Boolean {
        return oldItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: SubCategory, newItem: SubCategory): Boolean {
        return oldItem == newItem
    }
}