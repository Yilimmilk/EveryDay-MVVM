package cn.mapotofu.everydaymvvm.viewmodel.state

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.data.model.bean.GradeResp
import com.drake.brv.item.ItemExpand
import com.drake.brv.item.ItemHover
import com.drake.brv.item.ItemPosition
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2022/1/1
 */
class GradeViewModel : BaseViewModel() {
    //成绩原报文
    var gradeResponse = MutableLiveData<GradeResp>()
    //基本信息
    val stuId = MutableLiveData(appViewModel.studentInfo.value?.studentId)
    val cliToken = MutableLiveData(appViewModel.studentInfo.value?.token)
    var useCache = MutableLiveData(true)
    var reqScheduleYear = MutableLiveData(appViewModel.clientConf.value?.scheduleSemester?.substring(IntRange(0,3)))
    var reqScheduleTerm = MutableLiveData(appViewModel.clientConf.value?.scheduleSemester?.substring(IntRange(4,4)))

    data class GradeModel(
        var courseTitle: String = "",
        var classId: String = "",
        var courseNature: String = "",
        var credit: String = "0.0",
        var grade: String = "0",
        var gradePoint: String = "0.0",
        var gradeNature: String = "",
    ):  ItemHover, ItemPosition, BaseObservable() {
        var finalList: List<GradeDetailModel> = listOf()
        override var itemHover: Boolean = true
        override var itemPosition: Int = 0
    }

    data class GradeDetailModel(
        var itemName: String = "",
        var itemPercent: String = "",
        var itemScore: String = ""
    )
}