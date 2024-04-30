package com.example.pixelperfect.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Assets.FilterFileAsset.FiltersCode
import com.example.pixelperfect.Listener.FilterListener
import com.example.pixelperfect.R
import com.example.pixelperfect.Utils.Constants
import com.example.pixelperfect.Utils.SystemUtil
import com.github.siyamed.shapeimageview.RoundedImageView

class FilterAdapter(private val bitmapList: List<Bitmap>, var filterListener: FilterListener, private val context: Context, var filterBeanList: List<FiltersCode>) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    private val borderWidth: Int = SystemUtil.dpToPx(context, Constants.BORDER_WIDTH)
    var selectedFilterIndex = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_filter, viewGroup, false))
    }

    fun reset() {
        selectedFilterIndex = 0
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val imageView = viewHolder.roundImageView
        imageView.setImageBitmap(bitmapList[position])
        if (selectedFilterIndex == position) {
            imageView.setBorderColor(ContextCompat.getColor(context, R.color.mainColor))
            imageView.borderWidth = borderWidth
            imageView.maxWidth = 80
            imageView.maxHeight = 80
            return
        }
        imageView.setBorderColor(0)
        imageView.maxWidth = 70
        imageView.maxHeight = 70
        imageView.borderWidth = borderWidth
    }

    override fun getItemCount(): Int {
        return bitmapList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roundImageView: RoundedImageView = view.findViewById(R.id.round_image_view_filter_item)
        val relativeLayout: RelativeLayout = view.findViewById(R.id.relative_layout_wrapper_filter_item)

        init {
            view.setOnClickListener {
                val position = layoutPosition
                selectedFilterIndex = position
                filterListener.onFilterSelected(selectedFilterIndex, filterBeanList[position].code)
                notifyDataSetChanged()
            }
        }
    }
}
