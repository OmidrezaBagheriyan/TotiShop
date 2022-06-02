package com.omidrezabagherian.totishop.ui.house

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.databinding.ItemProductBinding
import com.omidrezabagherian.totishop.domain.model.product.Product

class HouseAdapter(private val details: (Product) -> Unit) :
    ListAdapter<Product, HouseAdapter.HouseViewHolder>(HouseDiffCall()) {

    class HouseViewHolder(
        private val itemProductBinding: ItemProductBinding,
        private val details: (Product) -> Unit
    ) : RecyclerView.ViewHolder(itemProductBinding.root) {
        fun bind(product: Product) {
            itemProductBinding.root.setOnClickListener {
                details(product)
            }

            Glide.with(itemProductBinding.root).load(product.images[0].src).fitCenter()
                .into(itemProductBinding.imageViewProductOrdinary)

            itemProductBinding.textViewProductName.text = product.name

            itemProductBinding.textViewProductOrdinary.text = "${product.price} تومان"

            if (product.regular_price == product.price) {
                itemProductBinding.cardViewProductPercent.visibility = View.GONE
                itemProductBinding.textViewProductPercent.visibility = View.GONE
                itemProductBinding.textViewProductOffer.visibility = View.GONE
            } else {
                val numPercent =
                    ((product.regular_price.toInt() - product.sale_price.toInt()) / (product.regular_price.toInt() / 100)).toString()
                itemProductBinding.textViewProductPercent.text = "$numPercent%"
                itemProductBinding.textViewProductOffer.text = "${product.regular_price} تومان"
                itemProductBinding.textViewProductOffer.paintFlags =
                    itemProductBinding.textViewProductOffer.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        return HouseViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), details
        )
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}