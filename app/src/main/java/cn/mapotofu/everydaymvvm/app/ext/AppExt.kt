package cn.mapotofu.everydaymvvm.app.ext

import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.hgj.jetpackmvvm.ext.navigateAction
import splitties.alertdialog.appcompat.title
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 *
 */
fun AppCompatActivity.showMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {},
    neutralButtonText: String = "",
    neutralAction: () -> Unit = {},
) {
    MaterialDialog(this)
        .cancelable(true)
        .lifecycleOwner(this)
        .show {
            title(text = title)
            message(text = message)
            positiveButton(text = positiveButtonText) {
                positiveAction.invoke()
            }
            if (negativeButtonText.isNotEmpty()) {
                negativeButton(text = negativeButtonText) {
                    negativeAction.invoke()
                }
            }
            if (neutralButtonText.isNotEmpty()) {
                neutralButton(text = neutralButtonText) {
                    neutralAction.invoke()
                }
            }
            getActionButton(WhichButton.POSITIVE)
            getActionButton(WhichButton.NEGATIVE)
            getActionButton(WhichButton.NEUTRAL)
        }
}

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 */
fun Fragment.showMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    activity?.let {
        MaterialDialog(it)
            .cancelable(true)
            .lifecycleOwner(viewLifecycleOwner)
            .show {
                title(text = title)
                message(text = message)
                positiveButton(text = positiveButtonText) {
                    positiveAction.invoke()
                }
                if (negativeButtonText.isNotEmpty()) {
                    negativeButton(text = negativeButtonText) {
                        negativeAction.invoke()
                    }
                }
                getActionButton(WhichButton.POSITIVE)
                getActionButton(WhichButton.NEGATIVE)
            }
    }
}

/**
 * @param view 显示对话框的主体View 必填项
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 * @param neutralButtonText 其他活动按钮文字 默认空 不为空时显示该按钮
 * @param neutralAction 点击其他活动按钮触发的方法 默认空方法
 */
fun AppCompatActivity.showCustomDialog(
    view: View,
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {},
    neutralButtonText: String = "",
    neutralAction: () -> Unit = {},
) {
    MaterialDialog(this)
        .cancelable(true)
        .lifecycleOwner(this)
        .show {
            customView(view = view)
            positiveButton(text = positiveButtonText) {
                positiveAction.invoke()
            }
            if (negativeButtonText.isNotEmpty()) {
                negativeButton(text = negativeButtonText) {
                    negativeAction.invoke()
                }
            }
            if (neutralButtonText.isNotEmpty()) {
                neutralButton(text = neutralButtonText) {
                    neutralAction.invoke()
                }
            }
            getActionButton(WhichButton.POSITIVE)
            getActionButton(WhichButton.NEGATIVE)
            getActionButton(WhichButton.NEUTRAL)
        }
}

/**
 * @param view 显示对话框的主体View 必填项
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 * @param neutralButtonText 其他活动按钮文字 默认空 不为空时显示该按钮
 * @param neutralAction 点击其他活动按钮触发的方法 默认空方法
 */
fun Fragment.showCustomDialog(
    view: View,
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {},
    neutralButtonText: String = "",
    neutralAction: () -> Unit = {},
) {
    activity?.let {
        MaterialDialog(it)
            .cancelable(true)
            .lifecycleOwner(viewLifecycleOwner)
            .show {
                customView(view = view)
                positiveButton(text = positiveButtonText) {
                    positiveAction.invoke()
                }
                if (negativeButtonText.isNotEmpty()) {
                    negativeButton(text = negativeButtonText) {
                        negativeAction.invoke()
                    }
                }
                if (neutralButtonText.isNotEmpty()) {
                    neutralButton(text = neutralButtonText) {
                        neutralAction.invoke()
                    }
                }
                getActionButton(WhichButton.POSITIVE)
                getActionButton(WhichButton.NEGATIVE)
                getActionButton(WhichButton.NEUTRAL)
            }
    }
}

/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

    }
    return null
}

/**
 * 拦截登录操作，如果没有登录跳转登录，登录过了贼执行你的方法
 */
fun NavController.jumpByLogin(action: (NavController) -> Unit) {
    if (CacheUtil.getIsLogin()) {
        action(this)
    } else {
        this.navigateAction(R.id.action_to_loginFragment)
    }
}

/**
 * 拦截登录操作，如果没有登录执行方法 actionLogin 登录过了执行 action
 */
fun NavController.jumpByLogin(
    actionLogin: (NavController) -> Unit,
    action: (NavController) -> Unit
) {
    if (CacheUtil.getIsLogin()) {
        action(this)
    } else {
        actionLogin(this)
    }
}


fun List<*>?.isNull(): Boolean {
    return this?.isEmpty() ?: true
}

fun List<*>?.isNotNull(): Boolean {
    return this != null && this.isNotEmpty()
}

/**
 * 根据索引获取集合的child值
 * @receiver List<T>?
 * @param position Int
 * @return T?
 */
inline fun <reified T> List<T>?.getChild(position: Int): T? {
    //如果List为null 返回null
    return if (this == null) {
        null
    } else {
        //如果position大于集合的size 返回null
        if (position + 1 > this.size) {
            null
        } else {
            //返回正常数据
            this[position]
        }
    }
}