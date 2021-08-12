package cn.mapotofu.everydaymvvm.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.base.BaseActivity
import cn.mapotofu.everydaymvvm.databinding.ActivityAboutBinding
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestAboutViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.AboutViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.activity
 * @author milk
 * @date 2021/8/12
 */
class AboutActivity : BaseActivity<AboutViewModel, ActivityAboutBinding>() {
    private val requestAboutViewModel: RequestAboutViewModel by viewModels()

    override fun layoutId(): Int = R.layout.activity_about

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestAboutViewModel)
        mDatabind.viewmodel = mViewModel

    }

    override fun createObserver() {

    }
}