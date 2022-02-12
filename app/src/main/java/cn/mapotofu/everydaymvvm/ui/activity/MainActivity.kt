package cn.mapotofu.everydaymvvm.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.core.widget.NestedScrollView
import androidx.navigation.Navigation
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseActivity
import cn.mapotofu.everydaymvvm.app.util.Const
import cn.mapotofu.everydaymvvm.app.util.OthersUtil
import cn.mapotofu.everydaymvvm.app.util.getPrefer
import cn.mapotofu.everydaymvvm.data.model.bean.NoticeResp
import cn.mapotofu.everydaymvvm.databinding.ActivityMainBinding
import cn.mapotofu.everydaymvvm.ui.activity.settings.SettingsActivity
import cn.mapotofu.everydaymvvm.app.util.DrawablesUtil
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestMainViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.MainViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import kotlinx.android.synthetic.main.activity_main.*
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.network.manager.NetState
import kotlin.random.Random


typealias SnackBarActionListener = ((View) -> Unit)?

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private val requestMainViewModel: RequestMainViewModel by viewModels()

    private var mAgentWeb: AgentWeb? = null
    var exitTime = 0L

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestMainViewModel)
        setSupportActionBar(bottom_app_bar)

        val navController = Navigation.findNavController(this@MainActivity, R.id.nav_frag)
        val bottomDrawerBehavior = BottomSheetBehavior.from(bottom_drawer)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        navController.addOnDestinationChangedListener { _, destination, _ ->
            nav_view.setCheckedItem(destination.id)
            fun setAllGone(){
                top_app_bar.visibility = View.GONE
                fab.visibility = View.GONE
                bottom_app_bar.visibility = View.GONE
                bottom_drawer.visibility = View.GONE
            }
            fun setAllInVisible(){
                top_app_bar.visibility = View.INVISIBLE
                fab.visibility = View.INVISIBLE
                bottom_app_bar.visibility = View.INVISIBLE
                bottom_drawer.visibility = View.INVISIBLE
            }
            fun setAllVisible(){
                top_app_bar.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
                bottom_app_bar.visibility = View.VISIBLE
                bottom_drawer.visibility = View.VISIBLE
            }
            when (destination.id) {
                R.id.splashFragment -> setAllGone()
                R.id.loginFragment -> setAllGone()
                R.id.loadScheduleFragment -> setAllGone()
                R.id.addCourseFragment -> setAllGone()
                else -> setAllVisible()
            }
        }

        bottom_app_bar.setNavigationOnClickListener {
            if (bottomDrawerBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }else {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        bottom_app_bar.fabAnimationMode = BottomAppBar.FAB_ANIMATION_MODE_SLIDE

        nav_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scheduleFragment -> {
                    navController.navigate(R.id.action_to_scheduleFragment)
                }
                R.id.gradeFragment -> {
                    navController.navigate(R.id.action_to_gradeFragment)
                }
                R.id.settingsActivity -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.aboutActivity -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
            }
            menuItem.isChecked = true
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            true
        }

        //设置侧滑抽屉图标彩色
        nav_view.itemIconTintList = null
        //返回键监听
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination!!.id == R.id.splashFragment ||
                    navController.currentDestination!!.id == R.id.loginFragment ||
                    navController.currentDestination!!.id == R.id.loadScheduleFragment ||
                    navController.currentDestination!!.id == R.id.scheduleFragment ||
                    navController.currentDestination!!.id == R.id.gradeFragment
                ) {
                    //是根页面
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        makeSnackBar("再按一次以退出程序～")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                } else {
                    //不是根页面
                    navController.navigateUp()
                }
            }
        })
        //请求通知内容
        requestMainViewModel.noticeReq()

        appViewModel.studentInfo.value?.let {
            val headerView = nav_view.getHeaderView(0)
            headerView.findViewById<TextView>(R.id.studentDrawerName)?.text = it.name
            headerView.findViewById<TextView>(R.id.studentDrawerID)?.text = it.studentId
        }
    }

    override fun createObserver() {
        //学生信息
        appViewModel.studentInfo.observeInActivity(this) {
            if (it != null) {
                val headerView = nav_view.getHeaderView(0)
                headerView.findViewById<TextView>(R.id.studentDrawerName)?.text = it.name
                headerView.findViewById<TextView>(R.id.studentDrawerID)?.text = it.studentId
            }
        }
        //通知回调
        requestMainViewModel.noticeResult.observeForever { resultState ->
            parseState(resultState, {
                it.notice?.let { it1 ->
                    it1.forEach { it2 ->
                        when (it2.is_important) {
                            true -> {
                                if (!getPrefer().getBoolean("${Const.KEY_PREFIX_NOTICE_ID}${it2.id}", false)) {
                                    makeNoticeDialog(it2)
                                }
                            }

                            false -> {

                            }
                        }
                    }
                }
            }, {
                val errorLog = resources.getString(R.string.message_network_error_log)
                Log.e(TAG, "${resources.getString(R.string.get_notice_fail)}:${String.format(errorLog, it.errCode, it.errorMsg, it.errorLog)}")
            })
        }
    }

    /**
     * 在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
//        if (netState.isSuccess) {
//            Toast.makeText(applicationContext, "俺有网了！", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(applicationContext, "断网了兄弟", Toast.LENGTH_SHORT).show()
//        }
    }

    //通知系统
    private fun makeNoticeDialog(data: NoticeResp.NoticeBean) {
        val view = View.inflate(this,R.layout.view_message_dialog,null)
        val tvContent = view.findViewById<TextView>(R.id.textview_content)
        val svContainer = view.findViewById<NestedScrollView>(R.id.scrollview_container)
        val wvContainer = view.findViewById<FrameLayout>(R.id.container_webview)
        val fabBackToTop = view.findViewById<FloatingActionButton>(R.id.fab_back_to_top)

        tvContent.text = data.content
        if (data.inner_web_url.isNotEmpty()) {
            //有网页
            wvContainer.visibility = View.VISIBLE
            fabBackToTop.visibility = View.VISIBLE
            fabBackToTop.setOnClickListener {
                svContainer.fullScroll(View.FOCUS_UP);
            }
            mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(
                    wvContainer,
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                .useDefaultIndicator(-1, 3)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .interceptUnkownUrl()
                .createAgentWeb()
                .ready()
                .go(data.inner_web_url)
        }else {
            //无网页
            wvContainer.visibility = View.GONE
            fabBackToTop.visibility = View.GONE
        }

        val builder = MaterialAlertDialogBuilder(this, R.style.NoticeAlertDialogCustom)
            .setTitle(data.title)
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                getPrefer().edit {
                    putBoolean(
                        "${Const.KEY_PREFIX_NOTICE_ID}${data.id}",
                        false
                    )
                }
            }
            .setNegativeButton(resources.getString(R.string.never_notify)) { dialog, which ->
                getPrefer().edit {
                    putBoolean(
                        "${Const.KEY_PREFIX_NOTICE_ID}${data.id}",
                        true
                    )
                }
            }
        if (!(data.background_color_1 == "default" || data.background_color_2 == "default" || data.background_color_3 == "default")) {
            Log.d(TAG,"颜色不为空，开始渲染背景")
            val colorArray = IntArray(3)
            colorArray[0] = Color.parseColor(data.background_color_1)
            colorArray[1] = Color.parseColor(data.background_color_2)
            colorArray[2] = Color.parseColor(data.background_color_3)
            val orientationArray = GradientDrawable.Orientation.values()
            builder.background =
                DrawablesUtil.getDrawable(orientationArray.random(Random(Random.nextInt())), colorArray,12,0,0)
        }
        if (data.addition_action_title.isNotEmpty()) {
            builder.setNeutralButton(data.addition_action_title) { dialog, which ->
                if (data.addition_action_url.isNotEmpty())
                    OthersUtil.openUrl(data.addition_action_url)
                else
                    makeSnackBar(resources.getString(R.string.this_one_has_nothing))
            }
        }
        builder.show()
    }

    fun makeSnackBar(text:String) {
        Snackbar.make(root_layout, text, Snackbar.LENGTH_LONG)
            .setAnchorView(if (fab.visibility == View.VISIBLE) fab else bottom_app_bar)
            .show()
    }

    fun makeSnackBar(
        text: String,
        actionText: String,
        listener: SnackBarActionListener
    ) {
        Snackbar.make(root_layout, text, Snackbar.LENGTH_LONG)
            .setAnchorView(if (fab.visibility == View.VISIBLE) fab else bottom_app_bar)
            .setAction(actionText,listener)
            .show()
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}