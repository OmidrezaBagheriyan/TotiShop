package com.omidrezabagherian.totishop.ui.listproduct

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.databinding.ItemListProductBinding
import com.omidrezabagherian.totishop.domain.model.product.Product

class ListCategoryAdapter(private val details: (Product) -> Unit) :
    ListAdapter<Product, ListCategoryAdapter.ListCategoryViewHolder>(ListCategoryDiffCall()) {

    class ListCategoryViewHolder(
        private val itemListProductBinding: ItemListProductBinding,
        private val details: (Product) -> Unit
    ) : RecyclerView.ViewHolder(itemListProductBinding.root) {
        fun bind(product: Product) {
            itemListProductBinding.root.setOnClickListener {
                details(product)
            }

            Glide.with(itemListProductBinding.root).load(product.images[0].src).fitCenter()
                .into(itemListProductBinding.imageViewListProduct)

            itemListProductBinding.textViewListProductName.text = product.name

            itemListProductBinding.textViewListProductOrdinary.text = "${product.price} تومان"

            if (product.regular_price == product.price) {
                itemListProductBinding.cardViewListProductPercent.visibility = View.GONE
                itemListProductBinding.textViewListProductPercent.visibility = View.GONE
                itemListProductBinding.textViewListProductOffer.visibility = View.GONE
            } else {
                val numPercent =
                    ((product.regular_price.toInt() - product.sale_price.toInt()) / (product.regular_price.toInt() / 100)).toString()
                itemListProductBinding.textViewListProductPercent.text = "$numPercent%"
                itemListProductBinding.textViewListProductOffer.text =
                    "${product.regular_price} تومان"
                itemListProductBinding.textViewListProductOffer.paintFlags =
                    itemListProductBinding.textViewListProductOffer.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCategoryViewHolder {
        return ListCategoryViewHolder(
            ItemListProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), details
        )
    }

    override fun onBindViewHolder(holder: ListCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ListCategoryDiffCall : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}