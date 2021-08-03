package cn.mapotofu.everydaymvvm.app.event

import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.data.model.entity.ClientConf
import cn.mapotofu.everydaymvvm.data.model.entity.UserInfo
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class AppViewModel : BaseViewModel() {

    //App的账户信息
    var studentInfo = UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()
    //App的配置
    var clientConf = UnPeekLiveData.Builder<ClientConf>().setAllowNullValue(true).create()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        studentInfo.value = CacheUtil.getStuInfo()
        clientConf.value = CacheUtil.getClientConf()
    }
}