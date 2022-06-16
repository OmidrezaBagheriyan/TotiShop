package com.omidrezabagherian.totishop.ui.bag

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.databinding.ItemBagProductBinding
import com.omidrezabagherian.totishop.domain.model.order.LineItem

class BagAdapter(private val delete: (LineItem) -> Unit) :
    ListAdapter<LineItem, BagAdapter.BagViewHolder>(BagDiffCall()) {

    class BagViewHolder(
        private val itemBagProductBinding: ItemBagProductBinding,
        private val delete: (LineItem) -> Unit
    ) : RecyclerView.ViewHolder(itemBagProductBinding.root) {
        fun bind(lineItem: LineItem) {
            Glide.with(itemBagProductBinding.root).load(lineItem.meta_data[0].value)
                .into(itemBagProductBinding.imageViewListProduct)
            itemBagProductBinding.textViewListProductName.text = lineItem.name
            itemBagProductBinding.textViewListProductOrdinary.text = lineItem.price.toString()
            itemBagProductBinding.textViewBagQuantity.text = lineItem.quantity.toString()
            itemBagProductBinding.imageViewBagQuantityPlusOne.setOnClickListener {
                Toast.makeText(itemBagProductBinding.root.context, "+1", Toast.LENGTH_SHORT).show()
            }
            itemBagProductBinding.imageViewBagQuantityNagOne.setOnClickListener {
                Toast.makeText(itemBagProductBinding.root.context, "-1", Toast.LENGTH_SHORT).show()
            }
            itemBagProductBinding.imageViewBagRemove.setOnClickListener {
                Toast.makeText(itemBagProductBinding.root.context, "remove", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        return BagViewHolder(
            ItemBagProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), delete
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