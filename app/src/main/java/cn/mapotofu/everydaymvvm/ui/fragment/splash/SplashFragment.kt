package cn.mapotofu.everydaymvvm.ui.fragment.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.viewModels
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.data.model.entity.ClientConf
import cn.mapotofu.everydaymvvm.data.model.entity.UserInfo
import cn.mapotofu.everydaymvvm.databinding.FragmentSplashBinding
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestSplashViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.SplashViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.splash
 * @author milk
 * @date 2021/7/30
 */
class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {
    private val requestSplashViewModel: RequestSplashViewModel by viewModels()
    private val isLogin = CacheUtil.getIsLogin()
    private val localConfData = CacheUtil.getClientConf()
    private val countdownTime: Long = 500

    override fun layoutId() = R.layout.fragment_splash

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestSplashViewModel)
        mDatabind.viewmodel = mViewModel

        mDatabind.textviewVersion.text = localConfData?.version.toString()
        requestSplashViewModel.confReq()
    }

    override fun createObserver() {
        requestSplashViewModel.confResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                val confData = ClientConf(
                    it.version,
                    it.chooseSemester,
                    it.gradeSemester,
                    it.scheduleSemester,
                    it.isMaintenance,
                    it.isVacation,
                    it.canCourseChoose,
                    it.termStart,
                    it.nowWeek
                )
                appViewModel.clientConf.value = confData
                CacheUtil.setIsConfUpdate(true)
                if (localConfData?.version != confData.version){
                    CacheUtil.setClientConf(confData)
                    mDatabind.textviewVersion.text = confData.version.toString()
                    mDatabind.textviewVersionUpdateStatus.text = "更新成功"
                }else {
                    mDatabind.textviewVersionUpdateStatus.text = "无需更新"
                }
                if (isLogin)
                    countDownNavigate(R.id.action_splashFragment_to_scheduleFragment)
                else
                    countDownNavigate(R.id.action_splashFragment_to_loginFragment)
            }, {
                CacheUtil.setIsConfUpdate(false)
                mDatabind.textviewVersionUpdateStatus.text = "更新失败"
                if (isLogin)
                    countDownNavigate(R.id.action_splashFragment_to_scheduleFragment)
                else
                    countDownNavigate(R.id.action_splashFragment_to_loginFragment)
            })
        })
    }

    private fun countDownNavigate(resId:Int){
        object : CountDownTimer(countdownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                nav().navigateAction(resId)
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null;
//        }
    }
}