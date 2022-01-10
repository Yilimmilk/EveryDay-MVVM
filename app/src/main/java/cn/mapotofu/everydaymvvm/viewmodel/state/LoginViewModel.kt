package cn.mapotofu.everydaymvvm.viewmodel.state

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/7/29
 */
class LoginViewModel : BaseViewModel() {
    //学号密码
    var stuId = MutableLiveData<String>()
    var stuPasswd = MutableLiveData<String>()
    //登陆模式
    var tokenMode = MutableLiveData<Boolean>(false)

    //请求网络状态
    var requestInProgress = MutableLiveData<Boolean>(false)
    //隐私政策状态
    var privacyStatus = MutableLiveData<Boolean>(false)
}