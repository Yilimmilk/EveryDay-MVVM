package cn.mapotofu.everydaymvvm.ui.fragment.login

import android.content.DialogInterface
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.hideSoftKeyboard
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.app.util.Const
import cn.mapotofu.everydaymvvm.app.util.getPrefer
import cn.mapotofu.everydaymvvm.data.model.entity.UserInfo
import cn.mapotofu.everydaymvvm.databinding.FragmentLoginBinding
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestLoginViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.login
 * @author milk
 * @date 2021/7/29
 */
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    private val requestLoginViewModel: RequestLoginViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestLoginViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()
    }

    override fun createObserver() {
        requestLoginViewModel.loginResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mViewModel.requestInProgress.postValue(false)
                val stuInfo = UserInfo(
                    it.studentId!!,
                    it.name!!,
                    mViewModel.stuPasswd.value!!,
                    it.token!!
                )
                mViewModel.userInfo.postValue(stuInfo)
                //TODO 目测是数据倒灌引起的bug？无法在viewmodel内更新appviewmodel的数据，通过下面这行代码临时解决一下
                appViewModel.studentInfo.postValue(stuInfo)
                mViewModel.isLogin.postValue(true)
                setTransitionAnimate()
                nav().navigateAction(R.id.action_loginFragment_to_loadScheduleFragment)
            }, {
                mViewModel.requestInProgress.postValue(false)
                mViewModel.isLogin.postValue(false)
                val errorLog = resources.getString(R.string.message_network_error_log)
                showMessage("${resources.getString(R.string.login_fail_see_error_message_for_details)}\n\n${String.format(errorLog, it.errCode, it.errorMsg, it.errorLog)}", resources.getString(R.string.login_fail))
            })
        }
    }

    private fun privacyPolicy() {
        val text = resources.getText(R.string.privacyPolicy).toString()
        showMessage(
            text,
            "隐私协议1.0",
            "同意并继续",
            {
                userAgreement()
                appContext.getPrefer().edit { putBoolean(Const.KEY_PRIVACY_POLICY, true) }
            },
            "不同意",
            {
                appContext.getPrefer().edit { putBoolean(Const.KEY_PRIVACY_POLICY, false) }
                mDatabind.checkboxPrivacy.isChecked = false
            }
        )
    }

    private fun userAgreement() {
        val text = resources.getText(R.string.userAgreement).toString()
        showMessage(
            text,
            "用户协议1.0",
            "同意并继续",
            { appContext.getPrefer().edit { putBoolean(Const.KEY_USER_AGREEMENT, true) } },
            "不同意",
            {
                appContext.getPrefer().edit { putBoolean(Const.KEY_USER_AGREEMENT, false) }
                mDatabind.checkboxPrivacy.isChecked = false
            }
        )
    }

    inner class ProxyClick {
        fun login() {
            hideSoftKeyboard(activity)
            when {
                mViewModel.stuId.value.isNullOrEmpty() -> showMessage("密码咧？")
                mViewModel.stuId.value!!.length != 11 -> showMessage("学号位数不对啊，如果您是老师的话，请微信搜索'乐首义'小程序，本APP暂不支持教师登陆～")
                mViewModel.stuPasswd.value.isNullOrEmpty() -> showMessage("密码咧？")
                mViewModel.privacyStatus.value == false -> showMessage("请先同意隐私协议以及用户协议~")
                else -> {
                    requestLoginViewModel.loginReq(
                        mViewModel.stuId.value!!,
                        mViewModel.stuPasswd.value!!
                    )
                }
            }
        }

        var onPrivacyCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { v, isChecked ->
                if (isChecked) privacyPolicy()
                mViewModel.privacyStatus.postValue(isChecked)
            }
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