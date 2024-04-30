package com.example.pixelperfect.Adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Editor.RatioModel
import com.example.pixelperfect.R
import com.steelkiwi.cropiwa.AspectRatio
import java.util.Arrays

/**
 * 比例
 */
class AspectAdapter() : RecyclerView.Adapter<AspectAdapter.ViewHolder>() {
    var lastSelectedView = 0
    var listener: OnNewSelectedListener? = null
    var ratios: List<RatioModel> = listOf(
        RatioModel(10, 10, R.drawable.ic_crop_free, "Free"),
        RatioModel(5, 4, R.drawable.ic_crop_free, "5:4"),
        RatioModel(1, 1, R.drawable.ic_instagram_4_5, "1:1"),
        RatioModel(4, 3, R.drawable.ic_crop_free, "4:3"),
        RatioModel(4, 5, R.drawable.ic_instagram_4_5, "4:5"),
        RatioModel(1, 2, R.drawable.ic_crop_free, "1:2"),
        RatioModel(9, 16, R.drawable.ic_instagram_4_5, "Story"),
        RatioModel(16, 7, R.drawable.ic_movie, "Movie"),
        RatioModel(2, 3, R.drawable.ic_crop_free, "2:3"),
        RatioModel(4, 3, R.drawable.ic_fb_cover, "Post"),
        RatioModel(16, 6, R.drawable.ic_fb_cover, "Cover"),
        RatioModel(16, 9, R.drawable.ic_crop_free, "16:9"),
        RatioModel(3, 2, R.drawable.ic_crop_free, "3:2"),
        RatioModel(2, 3, R.drawable.ic_pinterest, "Post"),
        RatioModel(16, 9, R.drawable.ic_crop_youtube, "Cover"),
        RatioModel(9, 16, R.drawable.ic_crop_free, "9:16"),
        RatioModel(3, 4, R.drawable.ic_crop_free, "3:4"),
        RatioModel(16, 8, R.drawable.ic_crop_post_twitter, "Post"),
        RatioModel(16, 5, R.drawable.ic_crop_post_twitter, "Header"),
        RatioModel(10, 16, R.drawable.ic_crop_free, "A4"),
        RatioModel(10, 16, R.drawable.ic_crop_free, "A5")
    )
    var selectedRatio: RatioModel

    interface OnNewSelectedListener {
        fun onNewAspectRatioSelected(aspectRatio: AspectRatio?)
    }

    init {
        selectedRatio = ratios[0]
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_crop, viewGroup, false))
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val aspectRatioCustom = ratios[i]
        viewHolder.ratioView.setImageResource(aspectRatioCustom.selectedIem)
        if (i == lastSelectedView) {
            viewHolder.text_view_filter_name.text = aspectRatioCustom.name
            viewHolder.relativeLayoutCrop.setBackgroundResource(R.drawable.background_item_selected)
        } else {
            viewHolder.text_view_filter_name.text = aspectRatioCustom.name
            viewHolder.relativeLayoutCrop.setBackgroundResource(R.drawable.background_item)
        }
    }

    override fun getItemCount(): Int {
        return ratios.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var ratioView: ImageView
        var relativeLayoutCrop: RelativeLayout
        var text_view_filter_name: TextView

        init {
            ratioView = view.findViewById(R.id.image_view_aspect_ratio)
            text_view_filter_name = view.findViewById(R.id.text_view_filter_name)
            relativeLayoutCrop = view.findViewById(R.id.relativeLayoutCropper)
            relativeLayoutCrop.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (lastSelectedView != adapterPosition) {
                selectedRatio = ratios[adapterPosition]
                lastSelectedView = adapterPosition
                if (listener != null) {
                    listener!!.onNewAspectRatioSelected(selectedRatio)
                }
                notifyDataSetChanged()
            }
        }
    }
}
