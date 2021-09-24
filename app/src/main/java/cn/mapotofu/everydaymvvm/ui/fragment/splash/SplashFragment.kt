package cn.mapotofu.everydaymvvm.ui.fragment.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import cn.mapotofu.everydaymvvm.BuildConfig
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.showMessage
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
    private val countdownTime: Long = 0

    override fun layoutId() = R.layout.fragment_splash

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestSplashViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        if (localConfData == null) {
            mDatabind.textviewVersion.text = "未获取"
        }else {
            mDatabind.textviewVersion.text = localConfData.version.toString()
        }

        //获取配置文件并上报信息
        if (isLogin){
            //若已登陆，则进行带参请求
            requestSplashViewModel.confReq(
                CacheUtil.getStuInfo()?.studentId!!,
                CacheUtil.getStuInfo()?.token!!,
                Constants.CLIENT_TYPE,
                BuildConfig.VERSION_NAME
            )
        }else {
            //若未登陆，则进行无参请求
            requestSplashViewModel.confReq()
        }
    }

    override fun createObserver() {
        //配置回调
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
                    Snackbar.make(requireActivity().rootView,"配置文件更新完成", Snackbar.LENGTH_LONG).show()
                    Log.i("获取配置状态","更新成功:${confData.version}")
                }else {
                    // 不更新配置
                    Log.i("获取配置状态","成功，无需更新:${confData.version}")
                }
                //上报数据判断部分
                it.tokenValid?.let { it1 ->
                    isLogin = it1
                    if (it1) {
                        Log.i("上报状态","成功")
                    }else {
                        Log.w("上报状态","成功，Token过期")
                        CacheUtil.setIsLogin(false)
                        Snackbar.make(requireActivity().rootView,"哦豁登陆过期了，重新登录吧", Snackbar.LENGTH_LONG).show()
                        nav().navigate(R.id.action_to_loginFragment)
                    }
                }
                if (isLogin)
                    countDownNavigate(R.id.action_splashFragment_to_scheduleFragment)
                else
                    countDownNavigate(R.id.action_splashFragment_to_loginFragment)
            }, {
                //失败状态
                CacheUtil.setIsConfUpdate(false)
                Snackbar.make(requireActivity().rootView,"配置文件更新失败，将只能使用离线功能", Snackbar.LENGTH_LONG).show()
                Log.e("获取配置状态","失败:${it.errCode}:${it.errorMsg}:${it.errorLog}")
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
        fun offlinemode(){
            CacheUtil.setIsConfUpdate(false)
            Snackbar.make(requireActivity().rootView,"叮，离线模式～", Snackbar.LENGTH_LONG).show()
            Log.e("用户手动进入离线模式","")
            if (isLogin)
                countDownNavigate(R.id.action_splashFragment_to_scheduleFragment)
            else
                countDownNavigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}