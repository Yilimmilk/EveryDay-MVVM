package cn.mapotofu.everydaymvvm.ui.activity.settings.provider

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import cn.mapotofu.everydaymvvm.ui.activity.settings.items.BaseSettingItem
import cn.mapotofu.everydaymvvm.ui.activity.settings.items.CategoryItem
import cn.mapotofu.everydaymvvm.ui.activity.settings.items.SettingType
import splitties.dimensions.dip
import splitties.resources.color

class CategoryItemProvider : BaseItemProvider<BaseSettingItem>() {

    override val itemViewType: Int
        get() = SettingType.CATEGORY

    override val layoutId: Int
        get() = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LinearLayoutCompat(parent.context).apply {
            id = R.id.anko_layout
            orientation = LinearLayoutCompat.VERTICAL
            addView(View(context).apply {
                id = R.id.anko_view
            }, LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, UisUtil.getStatusBarHeight(context) + dip(48)))

            addView(LinearLayoutCompat(context).apply {
                setPadding(dip(16), dip(2), dip(16), dip(2))
                setBackgroundColor(color(R.color.colorPrimary))

                addView(AppCompatTextView(context).apply {
                    id = R.id.anko_text_view
                    textSize = 12f
                    setLines(1)
                    setTextColor(Color.WHITE)
                    gravity = Gravity.CENTER_VERTICAL
                    typeface = Typeface.DEFAULT_BOLD
                }, LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT))

            }, LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dip(16)
            })
        }
        view.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        return BaseViewHolder(view)
    }

    override fun convert(helper: BaseViewHolder, data: BaseSettingItem) {
        if (data == null) return
        val item = data as CategoryItem
        helper.setText(R.id.anko_text_view, item.name)
        helper.setGone(R.id.anko_view, !item.hasMarginTop)
    }

}