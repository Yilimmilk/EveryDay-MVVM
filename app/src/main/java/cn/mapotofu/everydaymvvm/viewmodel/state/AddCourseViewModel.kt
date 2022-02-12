package cn.mapotofu.everydaymvvm.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.mapotofu.everydaymvvm.app.database.AppDataBase
import cn.mapotofu.everydaymvvm.data.model.entity.Course
import cn.mapotofu.everydaymvvm.data.repository.AddCourseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.viewmodel.state
 * @author milk
 * @date 2021/8/6
 */
class AddCourseViewModel : BaseViewModel() {
    var courseIdText = MutableLiveData<String>()
    var courseNameText = MutableLiveData<String>()
    var teachersNameText = MutableLiveData<String>()
    var campusNameText = MutableLiveData<String>()
    var positionText = MutableLiveData<String>()
    var hoursCompositionText = MutableLiveData<String>()
    var scoreText = MutableLiveData<String>()
    var colorText = MutableLiveData<String>()
    var timePlanText = MutableLiveData<String>()

    //数据库相关
    var repository = AddCourseRepository(AppDataBase.getDataBaseInstance().courseDao())

    fun insertCourse(course: Course) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCourse(course)
        }
    }

    fun deleteCourse(uid: Int) {
        repository.deleteCourse(uid)
    }

    fun updateCourse(course: Course) {
        repository.updateCourse(course)
    }

    fun getCourse(uid: Int): Course {
        return repository.getCourse(uid)
    }

    companion object {
        val TAG: String = this::class.java.enclosingClass.simpleName
    }
}