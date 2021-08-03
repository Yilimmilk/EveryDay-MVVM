package cn.mapotofu.everydaymvvm.ui.fragment.login

import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.hideSoftKeyboard
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
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
                nav().navigateAction(R.id.action_loginFragment_to_loadFragment)
            }, {
                mViewModel.requestInProgress.postValue(false)
                CacheUtil.setIsLogin(false)
                showMessage(it.errorMsg)
            })
        })
    }

    inner class ProxyClick {
        fun login() {
            hideSoftKeyboard(activity)
            when {
                mViewModel.stuId.value.isNullOrEmpty() -> showMessage("学号咧？")
                mViewModel.stuPasswd.value.isNullOrEmpty() -> showMessage("密码咧？")
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
                mViewModel.privacyStatus.postValue(isChecked)
            }
    }
}