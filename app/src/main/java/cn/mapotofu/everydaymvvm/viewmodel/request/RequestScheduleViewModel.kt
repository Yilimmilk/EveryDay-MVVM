package cn.mapotofu.everydaymvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.network.apiService
import cn.mapotofu.everydaymvvm.data.model.bean.SemesterResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.request
 * @author milk
 * @date 2021/7/31
 */
class RequestScheduleViewModel : BaseViewModel() {
    var semesterResult = MutableLiveData<ResultState<SemesterResp>>()

    //请求学期表
    fun semesterReq(stuId: String) {
        request({ apiService.getSemesterList(stuId) }, semesterResult, false)
    }
}