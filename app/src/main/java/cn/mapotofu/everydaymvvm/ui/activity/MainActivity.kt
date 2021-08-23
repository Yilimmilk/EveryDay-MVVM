package cn.mapotofu.everydaymvvm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.base.BaseActivity
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.Const
import cn.mapotofu.everydaymvvm.app.util.getPrefer
import cn.mapotofu.everydaymvvm.databinding.ActivityMainBinding
import cn.mapotofu.everydaymvvm.ui.activity.settings.SettingsActivity
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestMainViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_drawer_title.view.*
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.network.manager.NetState

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private val requestMainViewModel: RequestMainViewModel by viewModels()

    var exitTime = 0L

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestMainViewModel)
        //设置侧滑抽屉
        val navController = Navigation.findNavController(this@MainActivity, R.id.nav_frag)
        //控制侧滑抽屉是否显示
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> mDatabind.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                R.id.loginFragment -> mDatabind.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                else -> mDatabind.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
        //设置侧滑抽屉导航控制
        mDatabind.navView.setupWithNavController(navController)
        //自定义选项监听
        mDatabind.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settingsActivity -> {
                    mDatabind.drawerLayout.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.aboutActivity -> {
                    mDatabind.drawerLayout.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }
        //设置侧滑抽屉图标彩色
        mDatabind.navView.itemIconTintList = null
        //返回键监听
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination!!.id == R.id.splashFragment ||
                    navController.currentDestination!!.id == R.id.loginFragment ||
                    navController.currentDestination!!.id == R.id.loadScheduleFragment ||
                    navController.currentDestination!!.id == R.id.scheduleFragment
                ) {
                    //是根页面
                    Log.d("返回键监听", "是根页面")
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        Snackbar.make(rootView,"再按一次以退出程序～",Snackbar.LENGTH_LONG).show()
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                } else {
                    //不是根页面
                    Log.d("返回键监听", "不是根页面")
                    navController.navigateUp()
                }
            }
        })
        requestMainViewModel.noticeReq()
    }

    override fun createObserver() {
        requestMainViewModel.noticeResult.observeForever { resultState ->
            parseState(resultState, {
                it.importantNotice?.let { it1 ->
                    if (!getPrefer().getBoolean("${Const.KEY_PREFIX_NOTICE_ID}${it1.id}", false)) {
                        //若未读
                        showMessage(
                            "${it1.content}\n\n${it1.date}",
                            it1.title,
                            "朕已阅且不再提醒",
                            {
                                getPrefer().edit{ putBoolean("${Const.KEY_PREFIX_NOTICE_ID}${it1.id}", true) }
                            },
                            "只阅了一点点",
                            {
                                getPrefer().edit{ putBoolean("${Const.KEY_PREFIX_NOTICE_ID}${it1.id}", false) }
                            }
                        )
                    }
                }
            }, {
                Log.e("获取通知出错",it.errorMsg)
            })
        }
    }

    /**
     * 在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(applicationContext, "俺有网了！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "断网了兄弟", Toast.LENGTH_SHORT).show()
        }
    }
}