package com.omidrezabagherian.totishop.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.databinding.ItemCategoryBinding
import com.omidrezabagherian.totishop.domain.model.category.Category

class CategoryAdapter(private val details: (Category) -> Unit) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCall()) {

    class CategoryViewHolder(
        private val itemCategoryBinding: ItemCategoryBinding,
        private val details: (Category) -> Unit
    ) : RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun bind(category: Category) {
            itemCategoryBinding.root.setOnClickListener {
                details(category)
            }

            Glide.with(itemCategoryBinding.root).load(category.image.src).centerInside()
                .into(itemCategoryBinding.imageViewCategoryIcon)

            itemCategoryBinding.textViewCategoryTitle.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            details
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}