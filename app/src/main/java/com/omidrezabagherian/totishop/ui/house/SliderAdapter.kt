package com.omidrezabagherian.totishop.ui.house

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.omidrezabagherian.totishop.R
import com.omidrezabagherian.totishop.domain.model.product.Image
import java.util.*

class SliderAdapter(private val context: Context) :
    PagerAdapter() {

    private val images = mutableListOf<Image>()

    fun setImages(images: List<Image>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = layoutInflater.inflate(R.layout.item_image, container, false)

        val imageView: ImageView = itemView.findViewById(R.id.imageViewSlider)

        Glide.with(context).load(images[position].src).centerCrop().into(imageView)

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}