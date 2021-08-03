package cn.mapotofu.everydaymvvm.ui.fragment.load

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.viewModels
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.app.util.DataBaseUtil
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.data.model.entity.UserInfo
import cn.mapotofu.everydaymvvm.databinding.FragmentLoadBinding
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestLoadViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.LoadViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.load
 * @author milk
 * @date 2021/8/1
 */
class LoadFragment : BaseFragment<LoadViewModel, FragmentLoadBinding>() {
    private val requestLoadViewModel: RequestLoadViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_load

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestLoadViewModel)
        mDatabind.viewmodel = mViewModel

        val stuId = appViewModel.studentInfo.value?.studentId
        val cliToken = appViewModel.studentInfo.value?.token
        val curScheduleYear = appViewModel.clientConf.value?.scheduleSemester?.substring(IntRange(0,3))
        val curScheduleTerm = appViewModel.clientConf.value?.scheduleSemester?.substring(IntRange(4,4))

        requestLoadViewModel.scheduleReq(stuId!!,curScheduleYear!!,curScheduleTerm!!,false,cliToken!!)
        requestLoadViewModel.timetableReq()

        countDownNavigate(R.id.action_loadFragment_to_scheduleFragment)
    }

    @SuppressLint("SetTextI18n")
    override fun createObserver() {
        var initComplete = false
        requestLoadViewModel.scheduleResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                mViewModel.initCourse(it)
                mDatabind.textviewContent.text = "课表初始化成功\n${mDatabind.textviewContent.text}"
            }, {
                mDatabind.textviewContent.text = "课表初始化失败\n${mDatabind.textviewContent.text}"
                showMessage("试试重启App？或者清除App所有数据后再试试吧。${it.errorMsg}","初始化课表失败")
            })
        })

        requestLoadViewModel.timetableResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                mViewModel.initTimeTable(it)
                mDatabind.textviewContent.text = "时间表初始化成功\n${mDatabind.textviewContent.text}"
            }, {
                mDatabind.textviewContent.text = "课表初始化失败\n${mDatabind.textviewContent.text}"
                showMessage("试试重启App？或者清除App所有数据后再试试吧。${it.errorMsg}","初始化时间表失败")
            })
        })
        nav().navigateAction(R.id.action_loadFragment_to_scheduleFragment)
    }

    private fun countDownNavigate(resId:Int){
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                nav().navigateAction(resId)
            }
        }.start()
    }
}