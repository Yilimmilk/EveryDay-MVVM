package cn.mapotofu.everydaymvvm.ui.adapter

import cn.mapotofu.everydaymvvm.ui.activity.settings.items.BaseSettingItem
import cn.mapotofu.everydaymvvm.ui.activity.settings.provider.*
import com.chad.library.adapter.base.BaseProviderMultiAdapter

class SettingItemAdapter : BaseProviderMultiAdapter<BaseSettingItem>() {

    init {
        addItemProvider(CategoryItemProvider())
        addItemProvider(HorizontalItemProvider())
        addItemProvider(SeekBarItemProvider())
        addItemProvider(SwitchItemProvider())
        addItemProvider(VerticalItemProvider())
    }

    override fun getItemType(data: List<BaseSettingItem>, position: Int): Int {
        return data[position].getType()
    }

}