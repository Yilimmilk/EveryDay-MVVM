package cn.mapotofu.everydaymvvm.ui.custom.colorpicker

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentManager
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.ui.adapter.ColorPickerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.view_btm_color_picker.*

/**
 * 参考项目地址：https://github.com/msasikanth/ColorSheet
 */

typealias ColorPickerListener = ((color: String) -> Unit)?

class ColorPicker : BottomSheetDialogFragment() {

    private var viewCorners: Float = 4F
    private var colorPickerAdapter: ColorPickerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) dismiss()
        return inflater.inflate(R.layout.view_btm_color_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog? ?: return
                val behavior = dialog.behavior
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = 0
                behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }
        })

        val gradientDrawable = GradientDrawable().apply {
            cornerRadii = floatArrayOf(viewCorners, viewCorners, viewCorners, viewCorners, 0f, 0f, 0f, 0f)
        }
        view.background = gradientDrawable

        if (colorPickerAdapter != null) {
            colorPickerList.adapter = colorPickerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        colorPickerAdapter = null
    }

    fun colorPicker(
        colors: MutableList<String>,
        selectedColor: String? = null,
        listener: ColorPickerListener
    ): ColorPicker {
        colorPickerAdapter = ColorPickerAdapter(this, colors, selectedColor, listener)
        return this
    }

    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, "ColorPicker")
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}
