package com.example.pixelperfect.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Assets.BrushColorAsset
import com.example.pixelperfect.R

class TextColorAdapter(private val context: Context, var colorListener: ColorListener) : RecyclerView.Adapter<TextColorAdapter.ViewHolder>() {
    var selectedSquareIndex = 0
    var squareViewList: MutableList<SquareView?> = mutableListOf()

    interface ColorListener {
        fun onColorSelected(position: Int, squareView: SquareView?)
    }

    init {
        val listColorBrush = BrushColorAsset.listColorBrush()
        for (i in 0 until listColorBrush.size - 2) {
            squareViewList.add(SquareView(Color.parseColor(listColorBrush[i]), true))
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_color_text, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val squareView = squareViewList[i]
        if (squareView!!.isColor) {
            viewHolder.squareView.setCardBackgroundColor(squareView.drawableId)
        } else {
            viewHolder.squareView.setCardBackgroundColor(squareView.drawableId)
        }
        if (selectedSquareIndex == i) {
            viewHolder.viewSelected.setVisibility(View.VISIBLE)
        } else {
            viewHolder.viewSelected.setVisibility(View.GONE)
        }
    }

    override fun getItemCount(): Int {
        return squareViewList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var squareView: CardView
        var viewSelected: ImageView
        var lockPro: ImageView? = null
        var wrapSquareView: ConstraintLayout

        init {
            squareView = view.findViewById(R.id.card)
            viewSelected = view.findViewById(R.id.view_selected)
            wrapSquareView = view.findViewById(R.id.constraint_layout_wrapper_square_view)
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            selectedSquareIndex = adapterPosition
            colorListener.onColorSelected(selectedSquareIndex, squareViewList[selectedSquareIndex])
            notifyDataSetChanged()
        }
    }

    fun setSelectedColorIndex(i: Int) {
        selectedSquareIndex = i
    }

    inner class SquareView(
        @JvmField var drawableId: Int,
        @JvmField var isColor: Boolean = false
    )
}
