package cn.mapotofu.everydaymvvm.ui.fragment.load_schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.viewModels
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.databinding.FragmentLoadScheduleBinding
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestLoadScheduleViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.LoadScheduleViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState
import java.util.*

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.load
 * @author milk
 * @date 2021/8/1
 */
class LoadScheduleFragment : BaseFragment<LoadScheduleViewModel, FragmentLoadScheduleBinding>() {
    private val requestLoadScheduleViewModel: RequestLoadScheduleViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_load_schedule

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestLoadScheduleViewModel)
        mDatabind.viewmodel = mViewModel
        val stuId = appViewModel.studentInfo.value?.studentId
        val cliToken = appViewModel.studentInfo.value?.token
        var useCache = false
        var reqScheduleYear = appViewModel.clientConf.value?.scheduleSemester?.substring(IntRange(0,3))
        var reqScheduleTerm = appViewModel.clientConf.value?.scheduleSemester?.substring(IntRange(4,4))

        if (arguments!=null){
            reqScheduleYear = requireArguments().getString("req-year", "")
            reqScheduleTerm = requireArguments().getString("req-term", "")
            useCache = requireArguments().getBoolean("use-cache",false)
        }
        requestLoadScheduleViewModel.scheduleReq(stuId!!,reqScheduleYear!!,reqScheduleTerm!!,useCache,cliToken!!)
        requestLoadScheduleViewModel.timetableReq()

        countDownNavigate(R.id.action_loadScheduleFragment_to_scheduleFragment)
    }

    @SuppressLint("SetTextI18n")
    override fun createObserver() {
        requestLoadScheduleViewModel.scheduleResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                mViewModel.initCourse(it)
                mDatabind.textviewContent.text = "课表初始化成功\n${mDatabind.textviewContent.text}"
            }, {
                mDatabind.textviewContent.text = "课表初始化失败\n${mDatabind.textviewContent.text}"
                showMessage("试试重启App？或者清除App所有数据后再试试吧。${it.errorMsg}","初始化课表失败")
            })
        })

        requestLoadScheduleViewModel.timetableResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                mViewModel.initTimeTable(it)
                mDatabind.textviewContent.text = "时间表初始化成功\n${mDatabind.textviewContent.text}"
            }, {
                mDatabind.textviewContent.text = "课表初始化失败\n${mDatabind.textviewContent.text}"
                showMessage("试试重启App？或者清除App所有数据后再试试吧。${it.errorMsg}","初始化时间表失败")
            })
        })
        nav().navigateAction(R.id.action_loadScheduleFragment_to_scheduleFragment)
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