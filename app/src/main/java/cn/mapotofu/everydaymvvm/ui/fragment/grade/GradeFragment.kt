package cn.mapotofu.everydaymvvm.ui.fragment.grade

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import cn.mapotofu.everydaymvvm.BR
import cn.mapotofu.everydaymvvm.R
import cn.mapotofu.everydaymvvm.app.appViewModel
import cn.mapotofu.everydaymvvm.app.base.BaseFragment
import cn.mapotofu.everydaymvvm.app.ext.showMessage
import cn.mapotofu.everydaymvvm.app.util.CacheUtil
import cn.mapotofu.everydaymvvm.app.util.DataMapsUtil
import cn.mapotofu.everydaymvvm.databinding.FragmentGradeBinding
import cn.mapotofu.everydaymvvm.ui.activity.MainActivity
import cn.mapotofu.everydaymvvm.viewmodel.request.RequestGradeViewModel
import cn.mapotofu.everydaymvvm.viewmodel.state.GradeViewModel
import com.drake.brv.utils.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_grade.view.*
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.fragment.grade
 * @author milk
 * @date 2022/1/1
 */
class GradeFragment : BaseFragment<GradeViewModel, FragmentGradeBinding>() {
    private val requestGradeViewModel: RequestGradeViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_grade

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestGradeViewModel)
        mDatabind.viewmodel = mViewModel
        setHasOptionsMenu(true)
        // 初始化BindingAdapter的默认绑定ID
        BRV.modelId = BR.model

        requireActivity().top_app_bar.title = resources.getString(R.string.grade)
        requireActivity().top_app_bar.subtitle =
            "${mViewModel.reqGradeYear.value}学年第${mViewModel.reqGradeTerm.value}学期"
        requireActivity().bottom_app_bar.setFabAlignmentModeAndReplaceMenu(
            BottomAppBar.FAB_ALIGNMENT_MODE_CENTER,
            R.menu.menu_grade
        )

        requestGradeViewModel.gradeReq(
            mViewModel.reqGradeYear.value!!,
            mViewModel.reqGradeTerm.value!!,
            mViewModel.useCache.value!!,
        )

        requireActivity().fab.setImageResource(R.drawable.ic_analytics_24dp)
        requireActivity().fab.setOnClickListener {
            (activity as MainActivity).makeSnackBar("成绩分析页，还没做出来:D")
        }
    }

    override fun createObserver() {
        requestGradeViewModel.gradeResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                mViewModel.gradeResponse.postValue(it)
                mDatabind.recyclerGradeList.linear().setup {
                    addType<GradeViewModel.GradeModel>(R.layout.item_grade)
                    R.id.fab_detail.onClick {
                        requestGradeViewModel.gradeDetailReq(
                            mViewModel.reqGradeYear.value!!,
                            mViewModel.reqGradeTerm.value!!,
                            getModel<GradeViewModel.GradeModel>().courseTitle,
                            getModel<GradeViewModel.GradeModel>().classId,
                            false,
                        )
                    }
                }.models = DataMapsUtil.dataMappingGradeRespToGradeModel(it)
                if (mViewModel.useCache.value == false) {
                    (activity as MainActivity).makeSnackBar("刷新成功")
                } else {
                    //仅首次采用缓存
                    mViewModel.useCache.postValue(false)
                }
            }, {
                val errorLog = resources.getString(R.string.message_network_error_log)
                showMessage(
                    "${resources.getString(R.string.get_grade_fail)}\n\n${
                        String.format(
                            errorLog,
                            it.errCode,
                            it.errorMsg,
                            it.errorLog
                        )
                    }", resources.getString(R.string.get_grade_fail)
                )
            })
        }
        requestGradeViewModel.gradeDetailResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { it1 ->
                val view = View.inflate(requireContext(), R.layout.view_grade_detail, null)
                val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerGradeDetail)
                recyclerView.linear().setup {
                    addType<GradeViewModel.GradeDetailModel>(R.layout.item_grade_detail)
                }.models = DataMapsUtil.dataMappingGradeDetailRespToGradeDetailModel(it1)
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.grade_detail))
                    .setView(view)
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->

                    }
                    .show()
            })
        }
        requestGradeViewModel.semesterResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, { it ->
                val listPopupSemesterSelector =
                    PopupMenu(requireContext(), requireActivity().bottom_app_bar)
                it.semesterList!!.forEach { value ->
                    listPopupSemesterSelector.menu.add(
                        Menu.NONE,
                        value.id,
                        Menu.NONE,
                        value.text
                    )
                }
                listPopupSemesterSelector.setOnMenuItemClickListener { item ->
                    it.semesterList[item.itemId].let { it1 ->
                        mViewModel.reqGradeYear.postValue(it1.year)
                        mViewModel.reqGradeTerm.postValue(it1.term)
                        requestGradeViewModel.gradeReq(
                            it1.year,
                            it1.term,
                            false,
                        )
                    }
                    true
                }
                listPopupSemesterSelector.show()
            }, {
                val errorLog = resources.getString(R.string.message_network_error_log)
                showMessage(
                    "${resources.getString(R.string.get_response_fail_try_again)}\n\n${String.format(errorLog, it.errCode, it.errorMsg, it.errorLog)}",
                    resources.getString(R.string.aha_get_error)
                )
            })
        }
    }

    private fun showSemesterMenu(stuId: String) {
        requestGradeViewModel.semesterReq(stuId)
        showLoading("正在请求学期表，等一下咯...")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_grade, menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.refresh_grade -> {
                mViewModel.reqGradeYear.postValue(appViewModel.clientConf.value?.gradeSemester?.substring(IntRange(0,3)))
                mViewModel.reqGradeTerm.postValue(appViewModel.clientConf.value?.gradeSemester?.substring(IntRange(4,4)))
                requestGradeViewModel.gradeReq(
                    mViewModel.reqGradeYear.value!!,
                    mViewModel.reqGradeTerm.value!!,
                    mViewModel.useCache.value!!
                )
            }
            R.id.switch_semester -> {
                appViewModel.studentInfo.value?.let { it1 -> showSemesterMenu(it1.studentId) }
            }
        }
        return true
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}