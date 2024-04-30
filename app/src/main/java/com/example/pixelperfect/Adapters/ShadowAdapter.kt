package com.example.pixelperfect.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Editor.Text.TextShadow
import com.example.pixelperfect.R

/**
 * 文字阴影
 */
class ShadowAdapter(private val context: Context, private val textShadowList: List<TextShadow>) : RecyclerView.Adapter<ShadowAdapter.ViewHolder>() {
    var mClickListener: ShadowItemClickListener? = null
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    var selectedItem = 0

    interface ShadowItemClickListener {
        fun onShadowItemClick(view: View?, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_font, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textShadow = textShadowList[position]
        holder.textShadow.setShadowLayer(textShadow.radius.toFloat(), textShadow.dx.toFloat(), textShadow.dy.toFloat(), textShadow.colorShadow)
        val backgroundResId = if (selectedItem != position) {
            R.drawable.border_view
        } else {
            R.drawable.border_black_view
        }
        holder.wrapperFontItems.background = ContextCompat.getDrawable(context, backgroundResId)
    }

    override fun getItemCount(): Int {
        return textShadowList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var textShadow: TextView
        var wrapperFontItems: ConstraintLayout

        init {
            textShadow = view.findViewById(R.id.text_view_font_item)
            wrapperFontItems = view.findViewById(R.id.constraint_layout_wrapper_font_item)
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) {
                mClickListener!!.onShadowItemClick(view, adapterPosition)
            }
            selectedItem = adapterPosition
            notifyDataSetChanged()
        }
    }
}
