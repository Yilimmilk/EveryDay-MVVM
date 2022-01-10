package cn.mapotofu.everydaymvvm.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.ui.custom.colorpicker.ColorPicker
import cn.mapotofu.everydaymvvm.ui.custom.colorpicker.ColorPickerListener
import kotlinx.android.synthetic.main.item_color_picker.view.*

internal class ColorPickerAdapter(
    private val dialog: ColorPicker?,
    private var colors: MutableList<String>,
    private val selectedColor: String?,
    private val listener: ColorPickerListener
) : RecyclerView.Adapter<ColorPickerAdapter.ColorItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val color = inflater.inflate(R.layout.item_color_picker, parent, false)
        return ColorItemViewHolder(color)
    }

    override fun getItemCount(): Int {
        return  colors.size
    }

    override fun onBindViewHolder(holder: ColorItemViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ColorItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bindView() {
            val color = colors[adapterPosition]
            bindColorView(color)
        }

        private fun bindColorView(color: String) {
            itemView.colorSelected.isVisible = selectedColor != null && selectedColor == color
            itemView.colorSelected.setImageResource(R.drawable.ic_checked_24dp)
            itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(Color.parseColor(color))
        }

        override fun onClick(v: View?) {
            listener?.invoke(colors[adapterPosition])
            dialog?.dismiss()
        }
    }
}
