package cn.mapotofu.everydaymvvm.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import cn.mapotofu.everydaymvvm.BR
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.base.BaseActivity
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.app.util.OthersUtil
import cn.mapotofu.everydaymvvm.data.model.entity.AboutItem
import cn.mapotofu.everydaymvvm.databinding.ActivityAboutBinding
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestAboutViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.AboutViewModel
import com.drake.brv.utils.BRV
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_about.*
import me.hgj.jetpackmvvm.ext.parseState

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
        // 初始化BindingAdapter的默认绑定ID
        BRV.modelId = BR.model

        loadAnimation(mDatabind.imageLogo)

        requestAboutViewModel.aboutReq()

        //设置开源项目(参考)适配器
        val opensourceReferList = Constants.LIST_OPENSOURCE_REFER
        mDatabind.recyclerOpensourceRefer.linear().setup {
            addType<AboutItem>(R.layout.item_about)
            onClick(R.id.root_view) {
                getModel<AboutItem>().url?.let {
                    if (it.isNotEmpty())
                        OthersUtil.openUrl(it)
                    else
                        Snackbar.make(this@AboutActivity.about_root_view,"别戳了，这条啥也没有",Snackbar.LENGTH_LONG).show()
                }
            }
        }.models = opensourceReferList
        //设置开源项目(引用)适配器
        val opensourceImplList = Constants.LIST_OPENSOURCE_IMPL
        mDatabind.recyclerOpensourceImpl.linear().setup {
            addType<AboutItem>(R.layout.item_about)
            onClick(R.id.root_view) {
                getModel<AboutItem>().url?.let {
                    if (it.isNotEmpty())
                        OthersUtil.openUrl(it)
                    else
                        Snackbar.make(this@AboutActivity.about_root_view,"别戳了，这条啥也没有",Snackbar.LENGTH_LONG).show()
                }
            }
        }.models = opensourceImplList
    }

    override fun createObserver() {
        requestAboutViewModel.aboutResult.observeForever { resultState ->
            parseState(resultState, {
                //设置关于我适配器
                mDatabind.recyclerAbout.linear().setup {
                    addType<AboutItem>(R.layout.item_about)
                    onClick(R.id.root_view) {
                        getModel<AboutItem>().url?.let { it1 ->
                            if (it1.isNotEmpty())
                                OthersUtil.openUrl(it1)
                            else
                                Snackbar.make(this@AboutActivity.about_root_view,"别戳了，这条啥也没有",Snackbar.LENGTH_LONG).show()
                        }
                    }
                }.models = DataMapsUtil.dataMappingAboutRespAboutMeToAboutItem(it.aboutMe)
                //设置QA适配器
                mDatabind.recyclerQA.linear().setup {
                    addType<AboutItem>(R.layout.item_about)
                    onClick(R.id.root_view) {
                        getModel<AboutItem>().url?.let { it1 ->
                            if (it1.isNotEmpty())
                                OthersUtil.openUrl(it1)
                            else
                                Snackbar.make(this@AboutActivity.about_root_view,"别戳了，这条啥也没有",Snackbar.LENGTH_LONG).show()
                        }
                    }
                }.models = DataMapsUtil.dataMappingAboutRespQAToAboutItem(it.qa)
            }, {
                Snackbar.make(this.about_root_view,"出错了:${it.errorMsg}",Snackbar.LENGTH_LONG).show()
            })
        }
    }

    private fun loadAnimation(view: View) {
        var rotate = 0F
        rotate = if (rotate == 0F) {
            360F
        } else {
            0F
        }
        val springForce = SpringForce(rotate)
        springForce.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
        springForce.stiffness = SpringForce.STIFFNESS_VERY_LOW
        val animation = SpringAnimation(view, SpringAnimation.ROTATION_Y)
        animation.spring = springForce
        animation.start()
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}