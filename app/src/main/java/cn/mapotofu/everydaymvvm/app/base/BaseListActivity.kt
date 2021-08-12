package cn.mapotofu.everydaymvvm.app.base

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.ext.dismissLoadingExt
import cn.mapotofu.everydaymvvm.app.ext.showLoadingExt
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import cn.mapotofu.everydaymvvm.app.util.UisUtil.getStatusBarHeight
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import splitties.dimensions.dip
import splitties.resources.styledColor

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.app.base
 * @author milk
 * @date 2021/8/12
 */
abstract class BaseListActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {

    lateinit var mainTitle: AppCompatTextView
    lateinit var searchView: AppCompatEditText
    protected var showSearch = false
    protected var textWatcher: TextWatcher? = null
    protected lateinit var mRecyclerView: RecyclerView
    lateinit var rootView: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
            } // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            } // Night mode is active, we're using dark theme
        }
        savedInstanceState?.remove("android:support:fragments")
        //必须先初始化Window相关参数，才能setContent
        super.onCreate(savedInstanceState)
        rootView = createView()
        setContentView(rootView)
    }

    private fun createView() = ConstraintLayout(this).apply {

        val outValue = TypedValue()
        theme.resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true)

        mRecyclerView = RecyclerView(context, null, R.attr.verticalRecyclerViewStyle).apply {
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        mainTitle = AppCompatTextView(context).apply {
            text = title
            gravity = Gravity.CENTER_VERTICAL
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
        }

        searchView = AppCompatEditText(context).apply {
            hint = "请输入……"
            textSize = 16f
            background = null
            gravity = Gravity.CENTER_VERTICAL
            visibility = View.GONE
            setLines(1)
            setSingleLine()
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            addTextChangedListener(textWatcher)
        }

        addView(mRecyclerView, ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT).apply {
            topToTop = ConstraintSet.PARENT_ID
            bottomToBottom = ConstraintSet.PARENT_ID
            startToStart = ConstraintSet.PARENT_ID
            endToEnd = ConstraintSet.PARENT_ID
        })

        addView(
            LinearLayoutCompat(context).apply {
            id = R.id.anko_layout
            setPadding(0, getStatusBarHeight(this@BaseListActivity), 0, 0)
            setBackgroundColor(styledColor(R.attr.colorSurface))

            addView(AppCompatImageButton(context).apply {
                setImageResource(R.drawable.ic_back_24dp)
                setBackgroundResource(outValue.resourceId)
                setPadding(dip(8))
                setColorFilter(styledColor(R.attr.colorOnBackground))
                setOnClickListener {
                    onBackPressed()
                }
            }, LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, dip(48)))

            addView(mainTitle,
                LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, dip(48)).apply {
                    weight = 1f
                })

            addView(searchView, LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, dip(48)).apply {
                weight = 1f
            })

            if (showSearch) {
                addView(AppCompatTextView(context).apply {
                    textSize = 20f
                    text = "搜索"
                    gravity = Gravity.CENTER
                    setBackgroundResource(outValue.resourceId)
                    setOnClickListener {
                        when (searchView.visibility) {
                            View.GONE -> {
                                mainTitle.visibility = View.GONE
                                searchView.visibility = View.VISIBLE
                                setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                                searchView.isFocusable = true
                                searchView.isFocusableInTouchMode = true
                                searchView.requestFocus()
                                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                                    .showSoftInput(searchView, 0)
                            }
                        }
                    }
                }, LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, dip(48)).apply {
                    marginEnd = dip(24)
                })
            }

        }, ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT).apply {
            topToTop = ConstraintSet.PARENT_ID
            startToStart = ConstraintSet.PARENT_ID
            endToEnd = ConstraintSet.PARENT_ID
        })
    }

    //重新设置状态栏大小
    fun resizeStatusBar(view: View) {
        UisUtil.resizeStatusBar(this, view)
    }

    //获取状态栏高度
    fun getStatusBarHeight(): Int {
        return UisUtil.getStatusBarHeight(this)
    }

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

    override fun onDestroy() {
        searchView.removeTextChangedListener(textWatcher)
        textWatcher = null
        super.onDestroy()
    }
}