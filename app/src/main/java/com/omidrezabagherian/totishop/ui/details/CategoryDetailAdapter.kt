package com.omidrezabagherian.totishop.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omidrezabagherian.totishop.databinding.ItemDetailCategoryBinding
import com.omidrezabagherian.totishop.domain.model.product.Category

class CategoryDetailAdapter(private val details: (Category) -> Unit) :
    ListAdapter<Category, CategoryDetailAdapter.CategoryViewHolder>(CategoryDetailDiffCall()) {

    class CategoryViewHolder(
        private val itemDetailCategoryBinding: ItemDetailCategoryBinding,
        private val details: (Category) -> Unit
    ) : RecyclerView.ViewHolder(itemDetailCategoryBinding.root) {
        fun bind(category: Category) {
            itemDetailCategoryBinding.root.setOnClickListener {
                details(category)
            }
            itemDetailCategoryBinding.textViewDetailCategoryName.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemDetailCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), details
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class CategoryDetailDiffCall : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}