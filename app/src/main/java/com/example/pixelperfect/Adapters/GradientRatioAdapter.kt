package com.example.pixelperfect.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.R

class GradientRatioAdapter(private val context: Context, var backgroundInstaListener: BackgroundListener) : RecyclerView.Adapter<GradientRatioAdapter.ViewHolder>() {
    var selectedSquareIndex = 0
    var squareViewList: MutableList<SquareView?> = mutableListOf()

    interface BackgroundListener {
        fun onBackgroundSelected(position: Int, squareView: SquareView?)
    }

    init {
        squareViewList.add(SquareView(R.drawable.background_blur, "Blur"))
        squareViewList.add(SquareView(R.drawable.gradient_1, "Gradient_1"))
        squareViewList.add(SquareView(R.drawable.gradient_2, "Gradient_2"))
        squareViewList.add(SquareView(R.drawable.gradient_3, "Gradient_3"))
        squareViewList.add(SquareView(R.drawable.gradient_4, "Gradient_4"))
        squareViewList.add(SquareView(R.drawable.gradient_5, "Gradient_5"))
        squareViewList.add(SquareView(R.drawable.gradient_6, "Gradient_6"))
        squareViewList.add(SquareView(R.drawable.gradient_7, "Gradient_7"))
        squareViewList.add(SquareView(R.drawable.gradient_8, "Gradient_8"))
        squareViewList.add(SquareView(R.drawable.gradient_9, "Gradient_9"))
        squareViewList.add(SquareView(R.drawable.gradient_10, "Gradient_10"))
        squareViewList.add(SquareView(R.drawable.gradient_11, "Gradient_11"))
        squareViewList.add(SquareView(R.drawable.gradient_12, "Gradient_12"))
        squareViewList.add(SquareView(R.drawable.gradient_13, "Gradient_13"))
        squareViewList.add(SquareView(R.drawable.gradient_14, "Gradient_14"))
        squareViewList.add(SquareView(R.drawable.gradient_15, "Gradient_15"))
        squareViewList.add(SquareView(R.drawable.gradient_16, "Gradient_16"))
        squareViewList.add(SquareView(R.drawable.gradient_17, "Gradient_17"))
        squareViewList.add(SquareView(R.drawable.gradient_18, "Gradient_18"))
        squareViewList.add(SquareView(R.drawable.gradient_19, "Gradient_19"))
        squareViewList.add(SquareView(R.drawable.gradient_20, "Gradient_20"))
        squareViewList.add(SquareView(R.drawable.gradient_21, "Gradient_21"))
        squareViewList.add(SquareView(R.drawable.gradient_22, "Gradient_22"))
        squareViewList.add(SquareView(R.drawable.gradient_23, "Gradient_23"))
        squareViewList.add(SquareView(R.drawable.gradient_24, "Gradient_24"))
        squareViewList.add(SquareView(R.drawable.gradient_25, "Gradient_25"))
        squareViewList.add(SquareView(R.drawable.gradient_26, "Gradient_26"))
        squareViewList.add(SquareView(R.drawable.gradient_27, "Gradient_27"))
        squareViewList.add(SquareView(R.drawable.gradient_28, "Gradient_28"))
        squareViewList.add(SquareView(R.drawable.gradient_29, "Gradient_29"))
        squareViewList.add(SquareView(R.drawable.gradient_30, "Gradient_30"))
        squareViewList.add(SquareView(R.drawable.gradient_31, "Gradient_31"))
        squareViewList.add(SquareView(R.drawable.gradient_32, "Gradient_32"))
        squareViewList.add(SquareView(R.drawable.gradient_33, "Gradient_33"))
        squareViewList.add(SquareView(R.drawable.gradient_34, "Gradient_34"))
        squareViewList.add(SquareView(R.drawable.gradient_35, "Gradient_35"))
        squareViewList.add(SquareView(R.drawable.gradient_36, "Gradient_36"))
        squareViewList.add(SquareView(R.drawable.gradient_37, "Gradient_37"))
        squareViewList.add(SquareView(R.drawable.gradient_38, "Gradient_38"))
        squareViewList.add(SquareView(R.drawable.gradient_39, "Gradient_39"))
        squareViewList.add(SquareView(R.drawable.gradient_40, "Gradient_40"))
        squareViewList.add(SquareView(R.drawable.gradient_41, "Gradient_41"))
        squareViewList.add(SquareView(R.drawable.gradient_42, "Gradient_42"))
        squareViewList.add(SquareView(R.drawable.gradient_43, "Gradient_43"))
        squareViewList.add(SquareView(R.drawable.gradient_44, "Gradient_44"))
        squareViewList.add(SquareView(R.drawable.gradient_45, "Gradient_45"))
        squareViewList.add(SquareView(R.drawable.gradient_46, "Gradient_46"))
        squareViewList.add(SquareView(R.drawable.gradient_47, "Gradient_47"))
        squareViewList.add(SquareView(R.drawable.gradient_48, "Gradient_48"))
        squareViewList.add(SquareView(R.drawable.gradient_49, "Gradient_49"))
        squareViewList.add(SquareView(R.drawable.gradient_50, "Gradient_50"))
        squareViewList.add(SquareView(R.drawable.gradient_51, "Gradient_51"))
        squareViewList.add(SquareView(R.drawable.gradient_52, "Gradient_52"))
        squareViewList.add(SquareView(R.drawable.gradient_53, "Gradient_53"))
        squareViewList.add(SquareView(R.drawable.gradient_54, "Gradient_54"))
        squareViewList.add(SquareView(R.drawable.gradient_55, "Gradient_55"))
        squareViewList.add(SquareView(R.drawable.gradient_56, "Gradient_56"))
        squareViewList.add(SquareView(R.drawable.gradient_57, "Gradient_57"))
        squareViewList.add(SquareView(R.drawable.gradient_58, "Gradient_58"))
        squareViewList.add(SquareView(R.drawable.gradient_59, "Gradient_59"))
        squareViewList.add(SquareView(R.drawable.gradient_60, "Gradient_60"))
        squareViewList.add(SquareView(R.drawable.gradient_61, "Gradient_61"))
        squareViewList.add(SquareView(R.drawable.gradient_62, "Gradient_62"))
        squareViewList.add(SquareView(R.drawable.gradient_63, "Gradient_63"))
        squareViewList.add(SquareView(R.drawable.gradient_64, "Gradient_64"))
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_square_view, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val squareView = squareViewList[position]
        if (squareView!!.isGradient) {
            viewHolder.squareView.setBackgroundColor(squareView.drawableId)
        } else {
            viewHolder.squareView.setBackgroundResource(squareView.drawableId)
        }
        if (selectedSquareIndex == position) {
            viewHolder.imageViewSelected.setVisibility(View.VISIBLE)
        } else {
            viewHolder.imageViewSelected.setVisibility(View.GONE)
        }
    }

    override fun getItemCount(): Int {
        return squareViewList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var squareView: View
        var imageViewSelected: ImageView
        var wrapSquareView: ConstraintLayout

        init {
            imageViewSelected = view.findViewById(R.id.imageSelection)
            squareView = view.findViewById(R.id.square_view)
            wrapSquareView = view.findViewById(R.id.constraint_layout_wrapper_square_view)
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            selectedSquareIndex = adapterPosition
            backgroundInstaListener.onBackgroundSelected(selectedSquareIndex, squareViewList[selectedSquareIndex])
            notifyDataSetChanged()
        }
    }

    inner class SquareView(
        @JvmField var drawableId: Int,
        @JvmField var text: String,
        @JvmField var isGradient: Boolean = false
    )
}
