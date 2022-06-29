package cn.mapotofu.everydaymvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.network.apiService
import cn.mapotofu.everydaymvvm.data.model.bean.ConfResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.request
 * @author milk
 * @date 2021/7/30
 */
class RequestSplashViewModel : BaseViewModel() {
    var confResult = MutableLiveData<ResultState<ConfResp>>()

    //获取配置以及数据上报，由于第一次登录时无参数，所以初始化所有参数为null，再使用可空类型进行传参
    fun confReq(
        clientVersion: String? = null
    ) {
        request(
            { apiService.getConf(clientVersion) },
            confResult,
            false
        )
    }
}