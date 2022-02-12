package cn.mapotofu.everydaymvvm.app.event

import android.util.Log
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.data.model.entity.ClientConf
import cn.mapotofu.everydaymvvm.data.model.entity.UserInfo
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.databind.StringObservableField

class AppViewModel : BaseViewModel() {
    //App的账户信息
    var studentInfo: UnPeekLiveData<UserInfo> = UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()
    //App的配置
    var clientConf: UnPeekLiveData<ClientConf> = UnPeekLiveData.Builder<ClientConf>().setAllowNullValue(true).create()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        Log.d("AppViewModel","开始初始化信息")
        studentInfo.value = CacheUtil.getStuInfo()
        clientConf.value = CacheUtil.getClientConf()
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}