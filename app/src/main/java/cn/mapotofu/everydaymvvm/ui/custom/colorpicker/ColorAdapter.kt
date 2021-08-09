package cn.mapotofu.everydaymvvm.ui.custom.colorpicker

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import cn.mapotofu.everydaymvvm.R
import kotlinx.android.synthetic.main.item_color_picker.view.*

internal class ColorAdapter(
    private val dialog: ColorPicker?,
    private var colors: IntArray,
    private val selectedColor: Int?,
    private val noColorOption: Boolean,
    private val listener: ColorPickerListener
) : RecyclerView.Adapter<ColorAdapter.ColorItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val color = inflater.inflate(R.layout.item_color_picker, parent, false)
        return ColorItemViewHolder(color)
    }

    override fun getItemCount(): Int {
        return if (noColorOption) {
            colors.size + 1
        } else {
            colors.size
        }
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

        private fun bindColorView(@ColorInt color: Int) {
            itemView.colorSelected.isVisible = selectedColor != null && selectedColor == color
            itemView.colorSelected.setImageResource(R.drawable.ic_checked_24dp)
            itemView.colorSelectedCircle.imageTintList = ColorStateList.valueOf(color)
        }

        override fun onClick(v: View?) {
            if (noColorOption) {
                if (adapterPosition == 0) {
                    listener?.invoke(ColorPicker.NO_COLOR)
                } else {
                    listener?.invoke(colors[adapterPosition - 1])
                }
            } else {
                listener?.invoke(colors[adapterPosition])
            }
            dialog?.dismiss()
        }
    }
}
