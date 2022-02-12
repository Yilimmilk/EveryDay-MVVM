package cn.mapotofu.everydaymvvm.viewmodel.state

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.appViewModel
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/7/29
 */
class MainViewModel : BaseViewModel() {
    val name = MutableLiveData<String>()
    val studentId = MutableLiveData<String>()

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}