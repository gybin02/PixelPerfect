package com.example.pixelperfect.Adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Editor.PhotoEditor
import com.example.pixelperfect.Listener.AdjustListener
import com.example.pixelperfect.R
import java.text.MessageFormat

/**
 * 调色
 */
class AdjustAdapter(private val context: Context, var adjustListener: AdjustListener) : RecyclerView.Adapter<AdjustAdapter.ViewHolder>() {
    var FILTER_CONFIG_TEMPLATE = "@adjust brightness 0 @adjust contrast 1 @adjust saturation 1 @adjust sharpen 0"
    var ADJUST = " @adjust brightness 0 @adjust contrast 1 @adjust saturation 1 @adjust sharpen 0 @adjust exposure 0 @adjust hue 0 "
    var adjustModelList: MutableList<AdjustModel> = mutableListOf()
    var selectedFilterIndex = 0

    inner class AdjustModel internal constructor(var index: Int, var name: String, var code: String, var icon: Drawable?, var minValue: Float, var originValue: Float, var maxValue: Float) {
        var intensity = 0f
        @JvmField
        var seekbarIntensity = 0.5f
        fun setSeekBarIntensity(photoEditor: PhotoEditor?, mFloat: Float, mBoolean: Boolean) {
            if (photoEditor != null) {
                seekbarIntensity = mFloat
                intensity = calcIntensity(mFloat)
                photoEditor.setFilterIntensityForIndex(intensity, index, mBoolean)
            }
        }

        fun calcIntensity(f: Float): Float {
            if (f <= 0.0f) {
                return minValue
            }
            if (f >= 1.0f) {
                return maxValue
            }
            return if (f <= 0.5f) {
                minValue + (originValue - minValue) * f * 2.0f
            } else maxValue + (originValue - maxValue) * (1.0f - f) * 2.0f
        }
    }

    init {
        init()
    }

    fun setSelectedAdjust(i: Int) {
        adjustListener.onAdjustSelected(adjustModelList!![i])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_adjust, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.text_view_adjust_name.text = adjustModelList[i].name
        viewHolder.image_view_adjust_icon.setImageDrawable(if (selectedFilterIndex != i) adjustModelList!![i]!!.icon else adjustModelList!![i]!!.icon)
        val white = context.resources.getColor(R.color.white)
        if (selectedFilterIndex == i) {
            viewHolder.relativeLayoutEdit.setBackgroundResource(R.drawable.background_item_selected)
            viewHolder.image_view_adjust_icon.setColorFilter(white)
            viewHolder.text_view_adjust_name.setTextColor(white)
        } else {
            viewHolder.relativeLayoutEdit.setBackgroundResource(R.drawable.background_item)
            viewHolder.image_view_adjust_icon.setColorFilter(white)
            viewHolder.text_view_adjust_name.setTextColor(white)
        }
    }

    override fun getItemCount(): Int {
        return adjustModelList.size
    }

    val filterConfig: String
        get() {
            val str = ADJUST
            return MessageFormat.format(
                str,
                adjustModelList!![0]!!.originValue.toString() + "",
                adjustModelList[1].originValue.toString() + "",
                adjustModelList[2].originValue.toString() + "",
                adjustModelList[3].originValue.toString() + "",
                adjustModelList[4].originValue.toString() + "", adjustModelList!![5]!!.originValue
            )
        }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var relativeLayoutEdit: RelativeLayout
        var image_view_adjust_icon: ImageView
        var text_view_adjust_name: TextView

        init {
            image_view_adjust_icon = view.findViewById(R.id.image_view_adjust_icon)
            text_view_adjust_name = view.findViewById(R.id.text_view_adjust_name)
            relativeLayoutEdit = view.findViewById(R.id.relativeLayoutEdit)
            view.setOnClickListener {
                selectedFilterIndex = this@ViewHolder.layoutPosition
                adjustListener.onAdjustSelected(adjustModelList!![selectedFilterIndex])
                notifyDataSetChanged()
            }
        }
    }

    val currentAdjustModel: AdjustModel
        get() = adjustModelList[selectedFilterIndex]

    private fun init() {
        adjustModelList = mutableListOf()
        adjustModelList.add(AdjustModel(0, context.getString(R.string.brightness), "brightness", context.getDrawable(R.drawable.ic_brightness), -1.0f, 0.0f, 1.0f))
        adjustModelList.add(AdjustModel(1, context.getString(R.string.contrast), "contrast", context.getDrawable(R.drawable.ic_contrast), 0.1f, 1.0f, 3.0f))
        adjustModelList.add(AdjustModel(2, context.getString(R.string.saturation), "saturation", context.getDrawable(R.drawable.ic_saturation), 0.0f, 1.0f, 3.0f))
        adjustModelList.add(AdjustModel(5, context.getString(R.string.hue), "hue", context.getDrawable(R.drawable.ic_hue), -2.0f, 0.0f, 2.0f))
        adjustModelList.add(AdjustModel(3, context.getString(R.string.sharpen), "sharpen", context.getDrawable(R.drawable.ic_sharpen), -1.0f, 0.0f, 10.0f))
        adjustModelList.add(AdjustModel(4, context.getString(R.string.exposure), "exposure", context.getDrawable(R.drawable.ic_exposure), -2.0f, 0.0f, 2.0f))
    }
}
