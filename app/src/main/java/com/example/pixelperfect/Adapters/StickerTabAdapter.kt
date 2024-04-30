package com.example.pixelperfect.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.pixelperfect.R

class StickerTabAdapter(viewPager: ViewPager?, private val context: Context) : RecyclerTabLayout.Adapter<StickerTabAdapter.ViewHolder?>(viewPager) {
    private val mAdapter = mViewPager.adapter
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_tab_sticker, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val drawables = listOf(
            R.drawable.amoji,
            R.drawable.chicken,
            R.drawable.child,
            R.drawable.christmas,
            R.drawable.cute,
            R.drawable.emoj,
            R.drawable.emoji,
            R.drawable.fruit,
            R.drawable.heart,
            R.drawable.loveday,
            R.drawable.plant,
            R.drawable.sticker,
            R.drawable.sweet,
            R.drawable.textcolor,
            R.drawable.textneon
        )
        viewHolder.imageView.setImageDrawable(context.getDrawable(drawables[position]))
        viewHolder.imageView.setSelected(position == currentIndicatorPosition)
    }

    override fun getItemCount(): Int {
        return mAdapter!!.count
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView

        init {
            imageView = view.findViewById(R.id.image)
            view.setOnClickListener { this@StickerTabAdapter.viewPager.currentItem = this@ViewHolder.adapterPosition }
        }
    }
}
