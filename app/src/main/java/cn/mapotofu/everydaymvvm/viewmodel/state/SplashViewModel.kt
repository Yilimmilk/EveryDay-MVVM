package cn.mapotofu.everydaymvvm.viewmodel.state

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.data.model.entity.ClientConf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/7/30
 */
class SplashViewModel : BaseViewModel() {
    val isLogin = MutableLiveData<Boolean>(CacheUtil.getIsLogin())
    val localConfData = MutableLiveData<ClientConf>(appViewModel.clientConf.value)
    val confUpdateStatus = MutableLiveData<Boolean>(false)

    init {
        isLogin.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d(TAG,"isLogin数据变化")
                CacheUtil.setIsLogin(it)
            }
        }
        localConfData.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d(TAG,"localConfData数据变化")
                // 更新配置
                appViewModel.clientConf.postValue(it)
                CacheUtil.setClientConf(it)
            }
        }
        confUpdateStatus.observeForever {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d(TAG,"confUpdateStatus数据变化")
                CacheUtil.setIsConfUpdate(it)
            }
        }
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}