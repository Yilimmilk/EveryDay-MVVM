package cn.mapotofu.everydaymvvm.app.network

import cn.mapotofu.everydaymvvm.data.model.bean.*
import retrofit2.http.*

/**
 * @description
 * @package cn.mapotofu.everydaymvvm.app.network
 * @author milk
 * @date 2021/7/29
 */
interface ApiService {
    //获取客户端登陆Token
    @FormUrlEncoded
    @POST("client/client-login/")
    suspend fun getClientToken(
        @Field("stu_num") stuNumber: String,
        @Field("stu_passwd") stuPassword: String
    ): BaseResponse<LoginTokenResp>

    //获取个人信息
    @GET("info/get-pinfo/")
    suspend fun getPersonalInfo(): BaseResponse<PersonalInfoResp>

    //获取课表
    @GET("info/get-schedule/")
    suspend fun getScheduleList(
        @Query("req_year") reqYear: String,
        @Query("req_term") reqTerm: String,
        @Query("use_cache") useCache: String
    ): BaseResponse<ScheduleResp>

    //获取成绩
    @GET("info/get-grade/")
    suspend fun getGradeList(
        @Query("req_year") reqYear: String,
        @Query("req_term") reqTerm: String,
        @Query("use_cache") useCache: String
    ): BaseResponse<GradeResp>

    //获取成绩详情
    @GET("info/get-grade-detail/")
    suspend fun getGradeDetail(
        @Query("req_year") reqYear: String,
        @Query("req_term") reqTerm: String,
        @Query("course_name") courseName: String,
        @Query("class_id") classId: String,
        @Query("use_cache") useCache: String
    ): BaseResponse<GradeDetailResp>

    //获取配置清单并上报客户端信息
    //由于不一定有参数传进来，所以为可空类型
    @GET("conf/get-conf/")
    suspend fun getConf(
        @Query("client_version") clientVersion: String?
    ): BaseResponse<ConfResp>

    //获取倒计时
    @GET("conf/get-countdown/")
    suspend fun getCountDown(): BaseResponse<CountDownResp>

    //获取通知
    @GET("conf/get-notice/")
    suspend fun getNotice(): BaseResponse<NoticeResp>

    //获取关于
    @GET("conf/get-about/")
    suspend fun getAbout(): BaseResponse<AboutResp>

    //获取学期
    @GET("conf/get-semester/")
    suspend fun getSemesterList(): BaseResponse<SemesterResp>

    //获取时间表
    @GET("conf/get-timetable/")
    suspend fun getTimeTable(): BaseResponse<TimeTableResp>
}