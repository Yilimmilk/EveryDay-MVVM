package cn.mapotofu.everydaymvvm.viewmodel.state

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.data.model.bean.GradeResp
import com.drake.brv.annotaion.ItemOrientation
import com.drake.brv.item.ItemDrag
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
    var reqGradeYear = MutableLiveData(appViewModel.clientConf.value?.gradeSemester?.substring(IntRange(0,3)))
    var reqGradeTerm = MutableLiveData(appViewModel.clientConf.value?.gradeSemester?.substring(IntRange(4,4)))

    data class GradeModel(
        var courseTitle: String = "",
        var classId: String = "",
        var courseNature: String = "",
        var credit: String = "0.0",
        var grade: String = "0",
        var gradePoint: String = "0.0",
        var gradeNature: String = "",
    ): ItemPosition,
        ItemDrag,
        BaseObservable() {
        var finalList: List<GradeDetailModel> = listOf()
        override var itemPosition: Int = 0
        override var itemOrientationDrag: Int = ItemOrientation.ALL
    }

    data class GradeDetailModel(
        var itemName: String = "",
        var itemPercent: String = "",
        var itemScore: String = ""
    )

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}