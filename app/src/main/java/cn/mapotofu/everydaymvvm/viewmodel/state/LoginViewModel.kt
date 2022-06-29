package cn.mapotofu.everydaymvvm.viewmodel.state

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.data.model.entity.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/7/29
 */
class LoginViewModel : BaseViewModel() {
    //学号密码
    val stuId = MutableLiveData<String>()
    val stuPasswd = MutableLiveData<String>()
    //请求网络状态
    val requestInProgress = MutableLiveData<Boolean>(false)
    //隐私政策状态
    val privacyStatus = MutableLiveData<Boolean>(false)
    //是否已登陆
    val isLogin = MutableLiveData<Boolean>(CacheUtil.getIsLogin())
    //学生信息
    val userInfo = MutableLiveData<UserInfo>()

    init {
        isLogin.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d(TAG,"isLogin数据变化")
                CacheUtil.setIsLogin(it)
            }
        }
        userInfo.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d(TAG,"userInfo数据变化")
                CacheUtil.setStuInfo(it)
                appViewModel.studentInfo.postValue(it)
            }
        }
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}