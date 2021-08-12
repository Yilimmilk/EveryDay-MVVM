package cn.mapotofu.everydaymvvm.ui.fragment.login

import android.content.DialogInterface
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.viewModels
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
        requestLoginViewModel.loginResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                mViewModel.requestInProgress.postValue(false)
                val stuInfo = UserInfo(
                    it.studentId!!,
                    it.name!!,
                    mViewModel.stuPasswd.value!!,
                    it.token!!,
                    Constants.CLIENT_TYPE
                )
                CacheUtil.setStuInfo(stuInfo)
                CacheUtil.setIsLogin(true)
                appViewModel.studentInfo.value = stuInfo
                nav().navigateAction(R.id.action_loginFragment_to_loadScheduleFragment)
            }, {
                mViewModel.requestInProgress.postValue(false)
                CacheUtil.setIsLogin(false)
                showMessage(it.errorMsg, "啊哈出错了")
            })
        })
    }

    private fun privacyPolicy() {
        val text = resources.getText(R.string.privacyPolicy).toString()
        showMessage(
            text,
            "隐私协议1.0",
            "同意并继续",
            {
                userAgreement()
                App.context.getPrefer().edit { putBoolean(Const.KEY_PRIVACY_POLICY, true) }
            },
            "不同意",
            {
                App.context.getPrefer().edit { putBoolean(Const.KEY_PRIVACY_POLICY, false) }
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
            { App.context.getPrefer().edit { putBoolean(Const.KEY_USER_AGREEMENT, true) } },
            "不同意",
            {
                App.context.getPrefer().edit { putBoolean(Const.KEY_USER_AGREEMENT, false) }
                mDatabind.checkboxPrivacy.isChecked = false
            }
        )
    }

    inner class ProxyClick {
        fun login() {
            hideSoftKeyboard(activity)
            when {
                mViewModel.stuId.value.isNullOrEmpty() -> showMessage("学号咧？")
                mViewModel.stuPasswd.value.isNullOrEmpty() -> showMessage("密码咧？")
                mViewModel.privacyStatus.value == false -> showMessage("请先同意隐私协议以及用户协议~")
                else -> {
                    mViewModel.requestInProgress.postValue(true)
                    requestLoginViewModel.loginReq(
                        mViewModel.stuId.value!!,
                        mViewModel.stuPasswd.value!!
                    )
                }
            }
        }

        var onPrivacyCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) privacyPolicy()
                mViewModel.privacyStatus.postValue(isChecked)
            }
    }
}