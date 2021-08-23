package cn.mapotofu.everydaymvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.network.apiService
import cn.mapotofu.everydaymvvm.data.model.bean.NoticeResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.request
 * @author milk
 * @date 2021/8/14
 */
class RequestMainViewModel: BaseViewModel() {
    var noticeResult = MutableLiveData<ResultState<NoticeResp>>()

    fun noticeReq() {
        request(
            { apiService.getNotice() }
            , noticeResult,
            false,
        )
    }
}