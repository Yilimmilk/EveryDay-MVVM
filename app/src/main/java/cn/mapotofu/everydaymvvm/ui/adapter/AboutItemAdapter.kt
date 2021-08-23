package cn.mapotofu.everydaymvvm.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.data.model.entity.AboutItem
import kotlinx.android.synthetic.main.item_about.view.*

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.adapter
 * @author milk
 * @date 2021/8/13
 */
class AboutItemAdapter(private val context: Context, private val aboutList: List<AboutItem>,private val onClickCallback: OnClickCallback) :
    RecyclerView.Adapter<AboutItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.textviewTitle
        val content: TextView = itemView.textviewContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_about, parent, false)
        itemView.setOnClickListener(ItemClickListener())
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return aboutList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = aboutList[position]
        holder.title.text = currentItem.title
        holder.content.text = currentItem.content
        //作为View的Description传递url，暂时没想到其他办法而已哈哈哈哈
        holder.itemView.contentDescription = currentItem.url
        holder.itemView.tag = position
    }

    inner class ItemClickListener: View.OnClickListener {
        override fun onClick(v: View?) {
            onClickCallback.onClick(v!!, v.tag as Int, v.contentDescription as String)
        }
    }
}

interface OnClickCallback {
    fun onClick(view: View, position: Int, url: String)
}