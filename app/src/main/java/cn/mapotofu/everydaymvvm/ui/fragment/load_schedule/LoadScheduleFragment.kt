package cn.mapotofu.everydaymvvm.ui.fragment.load_schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
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
        var reqScheduleYear =
            appViewModel.clientConf.value?.scheduleSemester?.substring(IntRange(0, 3))
        var reqScheduleTerm =
            appViewModel.clientConf.value?.scheduleSemester?.substring(IntRange(4, 4))

        if (arguments != null) {
            reqScheduleYear = requireArguments().getString("req-year", "")
            reqScheduleTerm = requireArguments().getString("req-term", "")
            useCache = requireArguments().getBoolean("use-cache", false)
        }

        requestLoadScheduleViewModel.scheduleReq(
            reqScheduleYear!!,
            reqScheduleTerm!!,
            useCache
        )
        requestLoadScheduleViewModel.timetableReq()

        countDownNavigate()
    }

    @SuppressLint("SetTextI18n")
    override fun createObserver() {
        requestLoadScheduleViewModel.scheduleResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mViewModel.initCourse(it)
                mDatabind.textviewContent.text =
                    "${resources.getString(R.string.init_schedule_success)}\n${mDatabind.textviewContent.text}"
            }, {
                mDatabind.textviewContent.text =
                    "${resources.getString(R.string.init_schedule_fail)}\n${mDatabind.textviewContent.text}"
                val errorLog = resources.getString(R.string.message_network_error_log)
                showMessage(
                    "${resources.getString(R.string.try_to_exit_and_login_again)}\n\n${
                        String.format(
                            errorLog,
                            it.errCode,
                            it.errorMsg,
                            it.errorLog
                        )
                    }",
                    resources.getString(R.string.init_schedule_fail),
                )
            })
        }

        requestLoadScheduleViewModel.timetableResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mViewModel.initTimeTable(it)
                mDatabind.textviewContent.text =
                    "${resources.getString(R.string.init_timetable_success)}\n${mDatabind.textviewContent.text}"
            }, {
                mDatabind.textviewContent.text =
                    "${resources.getString(R.string.init_timetable_fail)}\n${mDatabind.textviewContent.text}"
                val errorLog = resources.getString(R.string.message_network_error_log)
                showMessage(
                    "${resources.getString(R.string.try_to_exit_and_login_again)}\n\n${
                        String.format(
                            errorLog,
                            it.errCode,
                            it.errorMsg,
                            it.errorLog
                        )
                    }",
                    resources.getString(R.string.init_timetable_fail),
                )
            })
        }
    }

    private fun countDownNavigate() {
        object : CountDownTimer(7000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                setTransitionAnimate()
                nav().navigateAction(R.id.action_loadScheduleFragment_to_scheduleFragment)
            }
        }.start()
    }

    private fun setTransitionAnimate() {
        val transInflater = TransitionInflater.from(requireContext())
        exitTransition = transInflater.inflateTransition(R.transition.explode)
        enterTransition = transInflater.inflateTransition(R.transition.fade)
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}