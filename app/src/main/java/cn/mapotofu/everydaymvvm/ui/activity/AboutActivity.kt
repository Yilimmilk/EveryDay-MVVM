package cn.mapotofu.everydaymvvm.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.LinearLayoutManager
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.Constants
import cn.mapotofu.everydaymvvm.app.base.BaseActivity
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.app.util.OthersUtil
import cn.mapotofu.everydaymvvm.databinding.ActivityAboutBinding
import cn.mapotofu.everydaymvvm.ui.adapter.AboutItemAdapter
import cn.mapotofu.everydaymvvm.ui.adapter.OnClickCallback
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestAboutViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.AboutViewModel
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

        loadAnimation(mDatabind.imageLogo)

        requestAboutViewModel.aboutReq()

        //设置开源项目(参考)适配器
        val opensourceReferList = Constants.LIST_OPENSOURCE_REFER
        mDatabind.recyclerOpensourceRefer.adapter = AboutItemAdapter(this,opensourceReferList,ItemClickCallback())
        mDatabind.recyclerOpensourceRefer.layoutManager = LinearLayoutManager(this)
        mDatabind.recyclerOpensourceRefer.setHasFixedSize(true)
        //设置开源项目(引用)适配器
        val opensourceImplList = Constants.LIST_OPENSOURCE_IMPL
        mDatabind.recyclerOpensourceImpl.adapter = AboutItemAdapter(this,opensourceImplList,ItemClickCallback())
        mDatabind.recyclerOpensourceImpl.layoutManager = LinearLayoutManager(this)
        mDatabind.recyclerOpensourceImpl.setHasFixedSize(true)
    }

    override fun createObserver() {
        requestAboutViewModel.aboutResult.observeForever { resultState ->
            parseState(resultState, {
                //设置关于我适配器
                val aboutmeList = DataMapsUtil.dataMappingAboutRespAboutMeToAboutItem(it.aboutMe)
                mDatabind.recyclerAbout.adapter = AboutItemAdapter(this,aboutmeList,ItemClickCallback())
                mDatabind.recyclerAbout.layoutManager = LinearLayoutManager(this)
                mDatabind.recyclerAbout.setHasFixedSize(true)
                //设置QA适配器
                val qaList = DataMapsUtil.dataMappingAboutRespQAToAboutItem(it.qa)
                mDatabind.recyclerQA.adapter = AboutItemAdapter(this,qaList,ItemClickCallback())
                mDatabind.recyclerQA.layoutManager = LinearLayoutManager(this)
                mDatabind.recyclerQA.setHasFixedSize(true)
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

    //recycleview回调函数
    inner class ItemClickCallback: OnClickCallback {
        override fun onClick(view: View, position: Int, url: String) {
            if (url.isNotEmpty()){
                OthersUtil.openUrl(url)
            }else {
                Snackbar.make(this@AboutActivity.about_root_view,"别戳了，这条啥也没有",Snackbar.LENGTH_LONG).show()
            }
//            val tvTitle: TextView = view.findViewById(R.id.textviewTitle)
//            val tvContent: TextView = view.findViewById(R.id.textviewContent)
//            Toast.makeText(this@AboutActivity,"position:$position, name:${tvTitle.text},name:${tvContent.text}",Toast.LENGTH_SHORT).show()
        }
    }
}