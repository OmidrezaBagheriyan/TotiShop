package com.omidrezabagherian.totishop.ui.details

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omidrezabagherian.totishop.databinding.ItemDetailReviewBinding
import com.omidrezabagherian.totishop.domain.model.review.Review

class ReviewDetailAdapter :
    ListAdapter<Review, ReviewDetailAdapter.ReviewViewHolder>(ReviewDetailDiffCall()) {

    class ReviewViewHolder(
        private val itemDetailReviewBinding: ItemDetailReviewBinding
    ) : RecyclerView.ViewHolder(itemDetailReviewBinding.root) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(review: Review) {
            itemDetailReviewBinding.buttonReviewEdit.visibility = View.GONE
            itemDetailReviewBinding.buttonReviewDelete.visibility = View.GONE
            itemDetailReviewBinding.ratingReviewNumberRate.rating = review.rating.toFloat()
            itemDetailReviewBinding.textViewReviewNameAccount.text = review.reviewer
            itemDetailReviewBinding.textViewReviewDescription.text = Html.fromHtml(review.review, 0)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewViewHolder {
        return ReviewViewHolder(
            ItemDetailReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ReviewDetailDiffCall : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}