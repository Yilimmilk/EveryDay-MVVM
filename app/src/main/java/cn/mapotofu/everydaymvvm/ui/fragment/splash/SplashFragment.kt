package cn.mapotofu.everydaymvvm.ui.fragment.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.viewModels
import cn.mapotofu.everydaymvvm.BuildConfig
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.data.model.entity.ClientConf
import cn.mapotofu.everydaymvvm.databinding.FragmentSplashBinding
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestSplashViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.SplashViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
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
    private var isLogin = CacheUtil.getIsLogin()
    private val localConfData = CacheUtil.getClientConf()
    private val countdownTime: Long = 500

    override fun layoutId() = R.layout.fragment_splash

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestSplashViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        mDatabind.textviewVersion.text = localConfData?.version.toString()
        // 先请求配置文件
        requestSplashViewModel.confReq()
    }

    override fun createObserver() {
        requestSplashViewModel.confResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                //如果成功
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
                    // 更新配置
                    CacheUtil.setClientConf(confData)
                    mDatabind.textviewVersion.text = confData.version.toString()
                    mDatabind.textviewVersionUpdateStatus.text = "更新成功"
                    Log.i("获取配置状态","更新成功")
                }else {
                    // 不更新配置
                    mDatabind.textviewVersionUpdateStatus.text = "无需更新"
                    Log.i("获取配置状态","成功，无需更新")
                }
                // 若已登陆则数据上报，若未登陆则直接导航到登陆页
                if (isLogin) {
                    requestSplashViewModel.clientReport(
                        CacheUtil.getStuInfo()?.studentId!!,
                        CacheUtil.getStuInfo()?.token!!,
                        1,
                        BuildConfig.VERSION_NAME
                    )
                }else countDownNavigate(R.id.action_splashFragment_to_loginFragment)
            }, {
                //失败状态
                CacheUtil.setIsConfUpdate(false)
                mDatabind.textviewVersionUpdateStatus.text = "更新失败"
                Log.e("获取配置状态","失败:$it")
                //进行跳转
                if (isLogin)
                    countDownNavigate(R.id.action_splashFragment_to_scheduleFragment)
                else
                    countDownNavigate(R.id.action_splashFragment_to_loginFragment)
            })
        })
        requestSplashViewModel.reportResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                //如果成功
                isLogin = if (it.tokenValid) {
                    Log.i("上报状态","成功")
                    true
                }else {
                    Log.w("上报状态","成功，Token过期")
                    CacheUtil.setIsLogin(false)
                    Snackbar.make(requireActivity().rootView,"哦豁登陆过期了，重新登录吧", Snackbar.LENGTH_LONG).show()
                    false
                }
                //进行跳转
                if (isLogin)
                    countDownNavigate(R.id.action_splashFragment_to_scheduleFragment)
                else
                    countDownNavigate(R.id.action_splashFragment_to_loginFragment)
            }, {
                //失败状态
                Log.e("上报状态","失败:$it")
                //进行跳转
                if (isLogin)
                    countDownNavigate(R.id.action_splashFragment_to_scheduleFragment)
                else
                    countDownNavigate(R.id.action_splashFragment_to_loginFragment)
            })
        })
    }

    private fun countDownNavigate(resId:Int){
        object : CountDownTimer(countdownTime, 200) {
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

    inner class ProxyClick {
        fun offlinemode() {
            CacheUtil.setIsConfUpdate(false)
            Snackbar.make(requireActivity().rootView,"叮，离线模式",Snackbar.LENGTH_LONG).show()
            if (isLogin)
                nav().navigateAction(R.id.action_splashFragment_to_scheduleFragment)
            else
                nav().navigateAction(R.id.action_splashFragment_to_loginFragment)
            onDestroy()
        }
    }
}