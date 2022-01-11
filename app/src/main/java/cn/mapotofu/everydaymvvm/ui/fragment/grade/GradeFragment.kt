package cn.mapotofu.everydaymvvm.ui.fragment.grade

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.mapotofu.everydaymvvm.BR
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.app.util.DrawablesUtil
import cn.mapotofu.everydaymvvm.app.util.UisUtil
import cn.mapotofu.everydaymvvm.databinding.FragmentGradeBinding
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestGradeViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.GradeViewModel
import com.drake.brv.item.ItemExpand
import com.drake.brv.layoutmanager.HoverGridLayoutManager
import com.drake.brv.utils.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.view_grade_detail.*
import kotlinx.android.synthetic.main.view_grade_detail.view.*
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.grade
 * @author milk
 * @date 2022/1/1
 */
class GradeFragment : BaseFragment<GradeViewModel, FragmentGradeBinding>() {
    private val requestGradeViewModel: RequestGradeViewModel by viewModels()

    private lateinit var popupWindowGradeDetail: PopupWindow

    override fun layoutId(): Int = R.layout.fragment_grade

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestGradeViewModel)
        mDatabind.viewmodel = mViewModel
        // 初始化BindingAdapter的默认绑定ID
        BRV.modelId = BR.model

        requestGradeViewModel.gradeReq(
            mViewModel.stuId.value!!,
            mViewModel.reqScheduleYear.value!!,
            mViewModel.reqScheduleTerm.value!!,
            mViewModel.useCache.value!!,
            mViewModel.cliToken.value!!
        )
        // 抽屉按钮
        drawerButton.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.openDrawer(GravityCompat.START)
        }
        refreshButton.setOnClickListener {
            requestGradeViewModel.gradeReq(
                mViewModel.stuId.value!!,
                mViewModel.reqScheduleYear.value!!,
                mViewModel.reqScheduleTerm.value!!,
                false,
                mViewModel.cliToken.value!!
            )
        }
    }

    override fun createObserver() {
        requestGradeViewModel.gradeResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                mViewModel.gradeResponse.postValue(it)
                mDatabind.recyclerGradeList.linear().setup {
                    addType<GradeViewModel.GradeModel>(R.layout.item_grade)
                    R.id.buttonGradeDetail.onFastClick {
                        requestGradeViewModel.gradeDetailReq(
                            mViewModel.stuId.value!!,
                            mViewModel.reqScheduleYear.value!!,
                            mViewModel.reqScheduleTerm.value!!,
                            getModel<GradeViewModel.GradeModel>().courseTitle,
                            getModel<GradeViewModel.GradeModel>().classId,
                            false,
                            mViewModel.cliToken.value!!
                        )
                    }
                }.models = DataMapsUtil.dataMappingGradeRespToGradeModel(it)
            }, {
                showMessage("获取成绩失败，重新试试?${it.errorMsg}", "获取成绩失败")
            })
        })
        requestGradeViewModel.gradeDetailResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, { it1 ->
                val view: View = LayoutInflater.from(App.context).inflate(R.layout.view_grade_detail, null)
                popupWindowGradeDetail = PopupWindow(
                    view,
                    UisUtil.dip2px(App.context, 200F),
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                //设置外面可触
                popupWindowGradeDetail.isOutsideTouchable = true
                //设置可触
                popupWindowGradeDetail.isFocusable = false
                popupWindowGradeDetail.setBackgroundDrawable(
                    DrawablesUtil.getDrawable(
                        Color.WHITE,
                        30,
                        0,
                        Color.WHITE
                    )
                )
                popupWindowGradeDetail.isTouchable = true
                popupWindowGradeDetail.elevation = 8f
                popupWindowGradeDetail.showAtLocation(activity?.window?.decorView, Gravity.CENTER, 0, 0);

                val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerGradeDetail)
                val layoutManager = HoverGridLayoutManager(requireContext(), 2) // 2 则代表列表一行铺满要求跨度为2
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (position < 0) return 1 // 如果添加分割线可能导致position为负数
                        // 根据类型设置列表item跨度
                        return 1
                    }
                }
                recyclerView.layoutManager = layoutManager
                recyclerView.setup {
                    addType<GradeViewModel.GradeDetailModel>(R.layout.item_grade_detail)
                }.models = DataMapsUtil.dataMappingGradeDetailRespToGradeDetailModel(it1)
            })
        })
    }
}