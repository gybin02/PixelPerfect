package com.example.pixelperfect.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.Assets.BrushColorAsset.listColorBrush
import com.example.pixelperfect.R

class ColorRatioAdapter(private val context: Context, var backgroundInstaListener: BackgroundColorListener) : RecyclerView.Adapter<ColorRatioAdapter.ViewHolder>() {
    var selectedSquareIndex = 0
    var squareViews: MutableList<SquareView?> = mutableListOf()

    interface BackgroundColorListener {
        fun onBackgroundColorSelected(i: Int, squareView: SquareView?)
    }

    init {
        squareViews.add(SquareView(R.drawable.background_blur, "Blur"))
        val lstColorForBrush = listColorBrush()
        for (i in 0 until lstColorForBrush.size - 2) {
            squareViews.add(SquareView(Color.parseColor(lstColorForBrush[i]), "", true))
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_color_ratio, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val squareView = squareViews[i]
        if (squareView!!.isColor) {
            viewHolder.squareView.setBackgroundColor(squareView.drawableId)
        } else {
            viewHolder.squareView.setBackgroundResource(squareView.drawableId)
        }
        if (selectedSquareIndex == i) {
            viewHolder.imageViewSelected.setVisibility(View.VISIBLE)
        } else {
            viewHolder.imageViewSelected.setVisibility(View.GONE)
        }
    }

    override fun getItemCount(): Int {
        return squareViews.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var squareView: View
        var imageViewSelected: ImageView
        var wrapSquareView: RelativeLayout

        init {
            squareView = view.findViewById(R.id.square_view)
            imageViewSelected = view.findViewById(R.id.imageSelection)
            wrapSquareView = view.findViewById(R.id.filterRoot)
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            selectedSquareIndex = adapterPosition
            backgroundInstaListener.onBackgroundColorSelected(selectedSquareIndex, squareViews[selectedSquareIndex])
            notifyDataSetChanged()
        }
    }

    inner class SquareView(
        @JvmField var drawableId: Int,
        @JvmField var text: String,
        @JvmField var isColor: Boolean = false
    )
}
