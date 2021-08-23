package cn.mapotofu.everydaymvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.network.apiService
import cn.mapotofu.everydaymvvm.data.model.bean.AboutResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.request
 * @author milk
 * @date 2021/8/12
 */
class RequestAboutViewModel : BaseViewModel() {
    var aboutResult = MutableLiveData<ResultState<AboutResp>>()

    //请求关于
    fun aboutReq() {
        request(
            {
                apiService.getAbout()
            }, aboutResult,
            false
        )
    }
}