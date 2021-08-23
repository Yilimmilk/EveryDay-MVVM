package cn.mapotofu.everydaymvvm.ui.activity

import android.content.ClipData
import android.os.Bundle
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.base.BaseActivity
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.databinding.ActivityErrorBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_error.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.clipboardManager
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.activity
 * @author milk
 * @date 2021/7/29
 */
class ErrorActivity : BaseActivity<BaseViewModel, ActivityErrorBinding>() {
    override fun layoutId() = R.layout.activity_error

    override fun initView(savedInstanceState: Bundle?) {
        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        errorRestart.clickNoRepeat {
            config?.run {
                CustomActivityOnCrash.restartApplication(this@ErrorActivity, this)
            }
        }
        errorShowError.clickNoRepeat {
            CustomActivityOnCrash.getStackTraceFromIntent(intent)?.let {
                showMessage(
                    it,
                    "日志详情",
                    "复制",
                    {
                        val mClipData = ClipData.newPlainText("errorLog", it)
                        // 将ClipData内容放到系统剪贴板里。
                        clipboardManager?.setPrimaryClip(mClipData)
                        Snackbar.make(this.rootView,"已复制错误日志",Snackbar.LENGTH_LONG).show()
                    },
                    "取消"
                )
            }
        }
    }
}