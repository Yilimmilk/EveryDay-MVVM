package cn.mapotofu.everydaymvvm.ui.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.base.BaseActivity
import cn.mapotofu.everydaymvvm.app.util.StatusBarUtil
import cn.mapotofu.everydaymvvm.databinding.ActivityMainBinding
import cn.mapotofu.everydaymvvm.viewmodel.state.MainViewModel
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import me.hgj.jetpackmvvm.network.manager.NetState

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    var exitTime = 0L
    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        //设置侧滑抽屉
        val navController = Navigation.findNavController(this@MainActivity, R.id.nav_frag)
        //控制侧滑抽屉是否显示
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.splashFragment || destination.id == R.id.loginFragment) {
                mDatabind.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                mDatabind.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
        //设置侧滑抽屉导航控制
        mDatabind.navView.setupWithNavController(navController)
        //设置无ActionBar，设置状态栏透明
        //supportActionBar?.hide()
        setStatusBarTrans()
        //设置侧滑抽屉图标彩色
        mDatabind.navView.itemIconTintList = null
        //返回键监听
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination!!.id == R.id.splashFragment ||
                    navController.currentDestination!!.id == R.id.loginFragment ||
                    navController.currentDestination!!.id == R.id.loadScheduleFragment ||
                    navController.currentDestination!!.id == R.id.scheduleFragment) {
                    //是根页面
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        SnackbarUtils.with(rootView).setMessage("再按一次以退出程序～").show()
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
    }

    override fun createObserver() {

    }

    /**
     * 示例，在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(applicationContext, "我特么终于有网了啊!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "我特么怎么断网了!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStatusBarTrans() {
        //状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
}