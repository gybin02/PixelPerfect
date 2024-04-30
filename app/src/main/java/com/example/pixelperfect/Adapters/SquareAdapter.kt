package com.example.pixelperfect.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Assets.StickerFileAsset.loadBitmapFromAssets
import com.example.pixelperfect.R
import com.example.pixelperfect.Sticker.SplashSticker
import com.example.pixelperfect.Utils.Constants
import com.example.pixelperfect.Utils.SystemUtil

class SquareAdapter(private val context: Context, var splashChangeListener: SplashChangeListener, z: Boolean) : RecyclerView.Adapter<SquareAdapter.ViewHolder>() {
    private val borderWidth: Int
    var selectedSquareIndex = 0
    /**
     * 遮罩列表
     */
    var maskList: MutableList<SplashItem?> = mutableListOf()

    interface SplashChangeListener {
        fun onSelected(splashSticker: SplashSticker?)
    }

    init {
        borderWidth = SystemUtil.dpToPx(context, Constants.BORDER_WIDTH)
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_1.webp"), loadBitmapFromAssets(context, "blur/image_frame_1.webp")), R.drawable.image_mask_1))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_2.webp"), loadBitmapFromAssets(context, "blur/image_frame_2.webp")), R.drawable.image_mask_2))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_3.webp"), loadBitmapFromAssets(context, "blur/image_frame_3.webp")), R.drawable.image_mask_3))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_4.webp"), loadBitmapFromAssets(context, "blur/image_frame_4.webp")), R.drawable.image_mask_4))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_5.webp"), loadBitmapFromAssets(context, "blur/image_frame_5.webp")), R.drawable.image_mask_5))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_6.webp"), loadBitmapFromAssets(context, "blur/image_frame_6.webp")), R.drawable.image_mask_6))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_7.webp"), loadBitmapFromAssets(context, "blur/image_frame_7.webp")), R.drawable.image_mask_7))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_8.webp"), loadBitmapFromAssets(context, "blur/image_frame_8.webp")), R.drawable.image_mask_8))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_9.webp"), loadBitmapFromAssets(context, "blur/image_frame_9.webp")), R.drawable.image_mask_9))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_10.webp"), loadBitmapFromAssets(context, "blur/image_frame_10.webp")), R.drawable.image_mask_10))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_11.webp"), loadBitmapFromAssets(context, "blur/image_frame_11.webp")), R.drawable.image_mask_11))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_12.webp"), loadBitmapFromAssets(context, "blur/image_frame_12.webp")), R.drawable.image_mask_12))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_13.webp"), loadBitmapFromAssets(context, "blur/image_frame_13.webp")), R.drawable.image_mask_13))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_14.webp"), loadBitmapFromAssets(context, "blur/image_frame_14.webp")), R.drawable.image_mask_14))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_15.webp"), loadBitmapFromAssets(context, "blur/image_frame_15.webp")), R.drawable.image_mask_15))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_16.webp"), loadBitmapFromAssets(context, "blur/image_frame_16.webp")), R.drawable.image_mask_16))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_17.webp"), loadBitmapFromAssets(context, "blur/image_frame_17.webp")), R.drawable.image_mask_17))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_18.webp"), loadBitmapFromAssets(context, "blur/image_frame_18.webp")), R.drawable.image_mask_18))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_19.webp"), loadBitmapFromAssets(context, "blur/image_frame_19.webp")), R.drawable.image_mask_19))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_20.webp"), loadBitmapFromAssets(context, "blur/image_frame_20.webp")), R.drawable.image_mask_20))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_21.webp"), loadBitmapFromAssets(context, "blur/image_frame_21.webp")), R.drawable.image_mask_21))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_22.webp"), loadBitmapFromAssets(context, "blur/image_frame_22.webp")), R.drawable.image_mask_22))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_23.webp"), loadBitmapFromAssets(context, "blur/image_frame_23.webp")), R.drawable.image_mask_23))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_24.webp"), loadBitmapFromAssets(context, "blur/image_frame_24.webp")), R.drawable.image_mask_24))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_25.webp"), loadBitmapFromAssets(context, "blur/image_frame_25.webp")), R.drawable.image_mask_25))
        maskList.add(SplashItem(SplashSticker(loadBitmapFromAssets(context, "blur/image_mask_26.webp"), loadBitmapFromAssets(context, "blur/image_frame_26.webp")), R.drawable.image_mask_26))
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_splash, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.splash.setImageResource(maskList[i]!!.drawableId)
        if (selectedSquareIndex == i) {
            viewHolder.relativeLayoutImage.setBackgroundResource(R.drawable.background_item_selected)
            return
        }
        viewHolder.relativeLayoutImage.setBackgroundResource(R.drawable.background_item)
    }

    override fun getItemCount(): Int {
        return maskList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var splash: ImageView
        var relativeLayoutImage: RelativeLayout

        init {
            splash = view.findViewById(R.id.round_image_view_splash_item)
            relativeLayoutImage = view.findViewById(R.id.relativeLayoutImage)
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            selectedSquareIndex = adapterPosition
            if (selectedSquareIndex < 0) {
                selectedSquareIndex = 0
            }
            if (selectedSquareIndex >= maskList.size) {
                selectedSquareIndex = maskList.size - 1
            }
            splashChangeListener.onSelected(maskList[selectedSquareIndex]!!.splashSticker)
            notifyDataSetChanged()
        }
    }

    class SplashItem(var splashSticker: SplashSticker, var drawableId: Int)
}
