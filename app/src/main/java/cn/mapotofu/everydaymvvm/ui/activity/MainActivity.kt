package cn.mapotofu.everydaymvvm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.base.BaseActivity
import cn.mapotofu.everydaymvvm.databinding.ActivityMainBinding
import cn.mapotofu.everydaymvvm.ui.activity.settings.SettingsActivity
import cn.mapotofu.everydaymvvm.viewmodel.state.MainViewModel
import com.blankj.utilcode.util.SnackbarUtils
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_drawer_title.view.*
import me.hgj.jetpackmvvm.network.manager.NetState

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    var exitTime = 0L
    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
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
                        SnackbarUtils.with(rootView).setMessage("再按一次以退出程序～").show()
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
    }

    override fun createObserver() {

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