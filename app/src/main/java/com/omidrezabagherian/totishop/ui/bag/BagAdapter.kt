package com.omidrezabagherian.totishop.ui.bag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.databinding.ItemBagProductBinding
import com.omidrezabagherian.totishop.domain.model.order.LineItem

class BagAdapter(
    private val add: (LineItem) -> Unit,
    private val nag: (LineItem) -> Unit,
) :
    ListAdapter<LineItem, BagAdapter.BagViewHolder>(BagDiffCall()) {

    class BagViewHolder(
        private val itemBagProductBinding: ItemBagProductBinding,
        private val add: (LineItem) -> Unit,
        private val nag: (LineItem) -> Unit
    ) : RecyclerView.ViewHolder(itemBagProductBinding.root) {
        fun bind(lineItem: LineItem) {
            if (lineItem.meta_data.isNotEmpty()) {
                Glide.with(itemBagProductBinding.root).load(lineItem.meta_data[0].value)
                    .into(itemBagProductBinding.imageViewListProduct)
            }
            itemBagProductBinding.textViewListProductName.text = lineItem.name
            itemBagProductBinding.textViewListProductOrdinary.text =
                "${lineItem.price.toInt()} تومان"
            itemBagProductBinding.textViewBagQuantity.text = lineItem.quantity.toString()
            itemBagProductBinding.imageViewBagQuantityPlusOne.setOnClickListener {
                add(lineItem)
                itemBagProductBinding.textViewBagQuantity.text = (lineItem.quantity + 1).toString()
            }
            itemBagProductBinding.imageViewBagQuantityNagOne.setOnClickListener {
                nag(lineItem)
                itemBagProductBinding.textViewBagQuantity.text = (lineItem.quantity - 1).toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        return BagViewHolder(
            ItemBagProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), add, nag
        )
    }

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class BagDiffCall : DiffUtil.ItemCallback<LineItem>() {
    override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
        return oldItem == newItem
    }
}