package cn.mapotofu.everydaymvvm.ui.fragment.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import cn.mapotofu.everydaymvvm.BuildConfig
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.navigateAction
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.databinding.FragmentSplashBinding
import cn.mapotofu.everydaymvvm.ui.activity.MainActivity
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestSplashViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.SplashViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.splash
 * @author milk
 * @date 2021/7/30
 */
class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {
    private val requestSplashViewModel: RequestSplashViewModel by viewModels()
    private var countdownTimer: CountDownTimer? = null
    private val timeToOfflineMode: Long = 1500 //1.5秒
//    private val navOptions = NavOptions.Builder()
//        .setEnterAnim(R.anim.nav_default_enter_anim) //进入动画
//        .setExitAnim(R.anim.nav_default_exit_anim) //退出动画
//        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim) //弹出进入动画
//        .setPopExitAnim(R.anim.nav_default_pop_exit_anim) //弹出退出动画
//        .build()

    override fun layoutId() = R.layout.fragment_splash

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestSplashViewModel)
        mDatabind.viewmodel = mViewModel

        if (mViewModel.localConfData.value == null) {
            mDatabind.textviewVersion.text = "未获取"
        }else {
            mDatabind.textviewVersion.text = mViewModel.localConfData.value!!.version.toString()
        }

        //获取配置文件并上报信息
        if (mViewModel.isLogin.value == true){
            Log.d(TAG,"进行带参请求配置文件")
            //若已登陆，则进行带参请求
            requestSplashViewModel.confReq(
                BuildConfig.VERSION_NAME
            )
        }else {
            Log.d(TAG,"进行无参请求配置文件")
            //若未登陆，则进行无参请求
            requestSplashViewModel.confReq()
        }

        countdownTimer = object : CountDownTimer(timeToOfflineMode, 100) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if ((mViewModel.confUpdateStatus.value == false) && (mViewModel.isLogin.value == true)) {
                    (activity as MainActivity).makeSnackBar(
                        "网络不佳?",
                        "离线模式",
                        listener = {
                            CacheUtil.setIsConfUpdate(false)
                            (activity as MainActivity).makeSnackBar("离线模式，部分功能将不可用")
                            nav().navigateAction(R.id.action_splashFragment_to_scheduleFragment)
                        }
                    )
                }
            }
        }.start()
    }

    override fun createObserver() {
        requestSplashViewModel.confResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                //如果成功
                val confData = DataMapsUtil.dataMappingConfRespToClientConf(it)
                mViewModel.confUpdateStatus.postValue(true)
                Log.d(TAG, "配置获取成功，本地版本:${mViewModel.localConfData.value?.version},远程版本:${confData.version}")
                if (mViewModel.localConfData.value?.version != confData.version) {
                    // 更新配置
                    mViewModel.localConfData.postValue(confData)
                    //TODO 目测是数据倒灌引起的bug？无法在viewmodel内更新appviewmodel的数据，通过下面这行代码临时解决一下
                    appViewModel.clientConf.postValue(confData)
                    (activity as MainActivity).makeSnackBar("配置文件更新完成")
                    Log.i(TAG, "配置更新成功:${confData.version}")
                } else {
                    // 不更新配置
                    Log.i(TAG, "配置获取成功，但无需更新:${confData.version}")
                }
                //上报数据判断部分
                it.tokenValid?.let { it1 ->
                    mViewModel.isLogin.postValue(it1)
                    if (it1) {
                        Log.i(TAG, "数据上报成功")
                    } else {
                        Log.w(TAG, "数据上报成功，Token过期")
                        (activity as MainActivity).makeSnackBar("哦豁登录过期了，重新登录吧")
                        mViewModel.isLogin.postValue(false)
                        nav().navigateAction(R.id.action_to_loginFragment)
                    }
                }
                if (mViewModel.isLogin.value!!){
                    nav().navigateAction(R.id.action_splashFragment_to_scheduleFragment)
                }else{
                    nav().navigateAction(R.id.action_splashFragment_to_loginFragment)
                }
            }, {
                //失败状态
                mViewModel.confUpdateStatus.postValue(false)
                if (mViewModel.isLogin.value!!){
                    (activity as MainActivity).makeSnackBar("配置文件更新失败，将只能使用离线功能")
                    Log.e(TAG, "获取配置失败:\n${it.errCode}\n${it.errorMsg}\n${it.errorLog}")
                    nav().navigateAction(R.id.action_splashFragment_to_scheduleFragment)
                }else {
                    showMessage("配置文件更新失败，可能是因为网络问题，稍后再试试吧。\n\n" +
                            "错误代码:${it.errCode}\n" +
                            "错误消息:${it.errorMsg}\n" +
                            "错误日志:${it.errorLog}")
                    Log.e(TAG, "获取配置失败:\n${it.errCode}\n${it.errorMsg}\n${it.errorLog}")
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countdownTimer != null) {
            countdownTimer?.cancel();
            countdownTimer = null;
        }
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}