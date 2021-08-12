package cn.mapotofu.everydaymvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.network.apiService
import cn.mapotofu.everydaymvvm.data.model.bean.ClientReportResp
import cn.mapotofu.everydaymvvm.data.model.bean.ConfResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import retrofit2.http.Field

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.request
 * @author milk
 * @date 2021/7/30
 */
class RequestSplashViewModel : BaseViewModel() {
    var confResult = MutableLiveData<ResultState<ConfResp>>()
    var reportResult = MutableLiveData<ResultState<ClientReportResp>>()

    fun confReq() {
        request(
            { apiService.getConf() },
            confResult,
            false
        )
    }

    fun clientReport(
        stuNumber: String,
        stuPassword: String,
        clientType: Int,
        clientVersion: String
    ) {
        request(
            { apiService.doClientReport(stuNumber, stuPassword, clientType, clientVersion) },
            reportResult,
            false
        )
    }
}