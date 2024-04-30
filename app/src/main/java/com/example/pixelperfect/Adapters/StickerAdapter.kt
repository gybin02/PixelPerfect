package com.example.pixelperfect.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pixelperfect.Assets.StickerFileAsset.loadBitmapFromAssets
import com.example.pixelperfect.R

/**
 * 贴纸数据源
 * 或者类似自己创建Json
 * https://www.flaticon.com/stickers-pack/birthday-209
 */
class StickerAdapter(var context: Context, var stickers: List<String>, var screenWidth: Int, var stickerListener: OnClickStickerListener) : RecyclerView.Adapter<StickerAdapter.ViewHolder>() {
    interface OnClickStickerListener {
        fun addSticker(i: Int, bitmap: Bitmap?)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sticker, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val bitmap = loadBitmapFromAssets(context, stickers[i])
        Glide.with(context).load(bitmap).into(viewHolder.sticker)
    }

    override fun getItemCount(): Int {
        return stickers.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var sticker: ImageView

        init {
            sticker = view.findViewById(R.id.image_view_item_sticker)
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            stickerListener.addSticker(adapterPosition, loadBitmapFromAssets(context, stickers[adapterPosition]))
        }
    }
}
