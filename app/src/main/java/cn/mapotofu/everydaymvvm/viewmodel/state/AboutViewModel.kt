package cn.mapotofu.everydaymvvm.viewmodel.state

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.BuildConfig
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/8/12
 */
class AboutViewModel: BaseViewModel() {
    val versionSlogan = MutableLiveData<String>("1.0.0")

    init {
        versionSlogan.value = "版本号:${BuildConfig.VERSION_NAME}\nPowered By Kotlin/Java\nBuild On JetPack"
    }
}