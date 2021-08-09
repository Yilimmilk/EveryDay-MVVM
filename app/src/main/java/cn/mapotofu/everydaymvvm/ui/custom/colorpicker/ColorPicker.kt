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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.view_btm_color_picker.*

/**
 * 原作者:Sasikanth Miriyampalli
 * 原项目地址：https://github.com/msasikanth/ColorSheet
 * 原开源协议：Apache-2.0 License
 * 修改：Yili
 */

/**
 * Listener for color picker
 *
 * returns color selected from the sheet. If noColorOption is enabled and user selects the option,
 * it will return [ColorSheet.NO_COLOR]
 */
typealias ColorPickerListener = ((color: Int) -> Unit)?

@Suppress("unused")
class ColorPicker : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "ColorSheet"
        const val NO_COLOR = -1
    }

    private var sheetCorners: Float = 4F
    private var colorAdapter: ColorAdapter? = null

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
            cornerRadii =
                floatArrayOf(sheetCorners, sheetCorners, sheetCorners, sheetCorners, 0f, 0f, 0f, 0f)
        }
        view.background = gradientDrawable

        if (colorAdapter != null) {
            colorSheetList.adapter = colorAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        colorAdapter = null
    }

    /**
     * Set corner radius of sheet top left and right corners.
     *
     * @param radius: Takes a float value
     */
    fun cornerRadius(radius: Float): ColorPicker {
        this.sheetCorners = radius
        return this
    }

    /**
     * Set corner radius of sheet top left and right corners.
     *
     * @param radius: Takes a float value
     */
    fun cornerRadius(radius: Int): ColorPicker {
        return cornerRadius(radius.toFloat())
    }

    /**
     * Config color picker
     *
     * @param colors: Array of colors to show in color picker
     * @param selectedColor: Pass in the selected color from colors list, default value is null. You can pass [ColorSheet.NO_COLOR]
     * to select noColorOption in the sheet.
     * @param noColorOption: Gives a option to set the [selectedColor] to [NO_COLOR]
     * @param listener: [ColorPickerListener]
     */
    fun colorPicker(
        colors: IntArray,
        @ColorInt selectedColor: Int? = null,
        noColorOption: Boolean = false,
        listener: ColorPickerListener
    ): ColorPicker {
        colorAdapter = ColorAdapter(this, colors, selectedColor, noColorOption, listener)
        return this
    }

    /**
     * Shows color sheet
     */
    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, TAG)
    }
}
