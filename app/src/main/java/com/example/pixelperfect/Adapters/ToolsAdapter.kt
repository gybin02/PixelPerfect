package com.example.pixelperfect.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pixelperfect.R
import com.example.pixelperfect.Utils.ToolEditor

class ToolsAdapter(var onItemSelected: OnItemSelected) : RecyclerView.Adapter<ToolsAdapter.ViewHolder>() {
    var toolLists: MutableList<ToolModel> = ArrayList()
    var selectedSquareIndex = 0

    interface OnItemSelected {
        fun onToolSelected(toolType: ToolEditor?)
    }

    init {
        toolLists.add(ToolModel(R.string.filter, R.drawable.ic_filter, ToolEditor.FILTER))
        toolLists.add(ToolModel(R.string.adjust, R.drawable.ic_set, ToolEditor.ADJUST))
        toolLists.add(ToolModel(R.string.overlay, R.drawable.ic_overlay, ToolEditor.EFFECT))
        toolLists.add(ToolModel(R.string.hsl, R.drawable.ic_hsl, ToolEditor.HSL))
        toolLists.add(ToolModel(R.string.crop, R.drawable.ic_crop, ToolEditor.CROP))
        toolLists.add(ToolModel(R.string.sticker, R.drawable.ic_sticker, ToolEditor.STICKER))
        toolLists.add(ToolModel(R.string.text, R.drawable.ic_text, ToolEditor.TEXT))
        toolLists.add(ToolModel(R.string.ratio, R.drawable.ic_ratio, ToolEditor.RATIO))
        toolLists.add(ToolModel(R.string.square, R.drawable.ic_blur_bg, ToolEditor.SQUARE))
    }

    inner class ToolModel(var toolName: Int, var toolIcon: Int, var toolType: ToolEditor)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_editing, viewGroup, false))
    }

    fun reset() {
        selectedSquareIndex = 0
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val toolModel = toolLists[i]
        viewHolder.text_view_tool_name.setText(toolModel.toolName)
        viewHolder.image_view_tool_icon.setImageResource(toolModel.toolIcon)

        /*if (this.selectedSquareIndex == i) {
            viewHolder.text_view_tool_name.setTextColor(Color.parseColor("#0004FF"));
            viewHolder.image_view_tool_icon.setColorFilter(Color.parseColor("#0004FF"));
            return;
        } else {
            viewHolder.text_view_tool_name.setTextColor(Color.parseColor("#000000"));
            viewHolder.image_view_tool_icon.setColorFilter(Color.parseColor("#000000"));
        }*/
    }

    override fun getItemCount(): Int {
        return toolLists.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image_view_tool_icon: ImageView
        var text_view_tool_name: TextView
        var relative_layout_wrapper_tool: RelativeLayout

        init {
            image_view_tool_icon = view.findViewById(R.id.image_view_tool_icon)
            text_view_tool_name = view.findViewById(R.id.text_view_tool_name)
            relative_layout_wrapper_tool = view.findViewById(R.id.relative_layout_wrapper_tool)
            relative_layout_wrapper_tool.setOnClickListener {
                selectedSquareIndex = this@ViewHolder.layoutPosition
                onItemSelected.onToolSelected(toolLists[selectedSquareIndex].toolType)
                notifyDataSetChanged()
            }
        }
    }
}
