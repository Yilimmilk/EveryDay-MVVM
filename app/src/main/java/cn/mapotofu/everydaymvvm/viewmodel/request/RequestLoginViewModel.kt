package cn.mapotofu.everydaymvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.network.apiService
import cn.mapotofu.everydaymvvm.data.model.bean.LoginTokenResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.state.ResultState
import me.hgj.jetpackmvvm.ext.request

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.request
 * @author milk
 * @date 2021/7/29
 */
class RequestLoginViewModel: BaseViewModel() {
    var loginResult = MutableLiveData<ResultState<LoginTokenResp>>()

    fun loginReq(stuId: String, stuPasswd: String) {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        request(
            { apiService.getClientToken(stuId,stuPasswd) }
            , loginResult,
            true,
            "别急，登陆在..."
        )
    }
}