package com.example.pixelperfect.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Assets.FontFileAsset.setFontByName
import com.example.pixelperfect.R

/**
 * 字体数据
 */
class FontAdapter(private val context: Context, private val fontList: List<String>) : RecyclerView.Adapter<FontAdapter.ViewHolder>() {
    var mClickListener: ItemClickListener? = null
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    var selectedItem = 0

    interface ItemClickListener {
        fun onItemClick(view: View?, i: Int)
    }

    override fun getItemViewType(i: Int): Int {
        return i
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_font, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setFontByName(context, holder.font, fontList[position])
        val backgroundResId = if (selectedItem != position) {
            R.drawable.border_view
        } else {
            R.drawable.border_black_view
        }
        holder.wrapperFontItems.background = ContextCompat.getDrawable(context, backgroundResId)
    }

    override fun getItemCount(): Int {
        return fontList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val font: TextView = view.findViewById(R.id.text_view_font_item)
        val wrapperFontItems: ConstraintLayout = view.findViewById(R.id.constraint_layout_wrapper_font_item)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            selectedItem = adapterPosition
            mClickListener?.onItemClick(view, selectedItem)
            notifyDataSetChanged()
        }
    }

}
