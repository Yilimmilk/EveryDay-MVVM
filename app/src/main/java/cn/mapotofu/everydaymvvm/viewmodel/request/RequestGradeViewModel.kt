package cn.mapotofu.everydaymvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.network.apiService
import cn.mapotofu.everydaymvvm.data.model.bean.GradeDetailResp
import cn.mapotofu.everydaymvvm.data.model.bean.GradeResp
import cn.mapotofu.everydaymvvm.data.model.bean.SemesterResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.request
 * @author milk
 * @date 2022/1/1
 */
class RequestGradeViewModel: BaseViewModel() {
    var gradeResult = MutableLiveData<ResultState<GradeResp>>()
    var gradeDetailResult = MutableLiveData<ResultState<GradeDetailResp>>()
    var semesterResult = MutableLiveData<ResultState<SemesterResp>>()

    //请求成绩
    fun gradeReq(
        year: String,
        term: String,
        useCache: Boolean
    ) {
        request(
            {
                apiService.getGradeList(
                    year,
                    term,
                    if (useCache) "yes" else "no"
                )
            }, gradeResult,
            true
        )
    }

    //请求成绩详情
    fun gradeDetailReq(
        year: String,
        term: String,
        courseName: String,
        classId: String,
        useCache: Boolean
    ) {
        request(
            {
                apiService.getGradeDetail(
                    year,
                    term,
                    courseName,
                    classId,
                    if (useCache) "yes" else "no"
                )
            }, gradeDetailResult,
            true
        )
    }

    //请求学期表
    fun semesterReq(stuId: String) {
        request({ apiService.getSemesterList() }, semesterResult, false)
    }
}