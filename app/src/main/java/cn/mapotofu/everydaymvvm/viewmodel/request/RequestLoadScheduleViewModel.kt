package cn.mapotofu.everydaymvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.network.apiService
import cn.mapotofu.everydaymvvm.data.model.bean.ScheduleResp
import cn.mapotofu.everydaymvvm.data.model.bean.TimeTableResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.request
 * @author milk
 * @date 2021/8/1
 */
class RequestLoadScheduleViewModel : BaseViewModel() {
    var scheduleResult = MutableLiveData<ResultState<ScheduleResp>>()
    var timetableResult = MutableLiveData<ResultState<TimeTableResp>>()

    //请求课表
    fun scheduleReq(
        year: String,
        term: String,
        useCache: Boolean
    ) {
        request(
            {
                apiService.getScheduleList(
                    year,
                    term,
                    if (useCache) "yes" else "no"
                )
            }, scheduleResult,
            false
        )
    }

    //请求时间表
    fun timetableReq() {
        request(
            {
                apiService.getTimeTable()
            }, timetableResult,
            false
        )
    }
}