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
    @POST("/client/client-login/")
    suspend fun getClientToken(
        @Field("stu_num") stuNumber: String,
        @Field("stu_passwd") stuPassword: String,
        @Field("client_type") clientType: Int
    ): BaseResponse<LoginTokenResp>

    //获取个人信息
    @GET("/info/get-pinfo")
    suspend fun getPersonalInfo(
        @Query("stu_num") stuNumber: String,
        @Query("cli_token") cliToken: String
    ): BaseResponse<PersonalInfoResp>

    //获取课表
    @GET("/info/get-schedule")
    suspend fun getScheduleList(
        @Query("stu_num") stuNumber: String,
        @Query("req_year") reqYear: String,
        @Query("req_term") reqTerm: String,
        @Query("use_cache") useCache: String,
        @Query("cli_token") cliToken: String
    ): BaseResponse<ScheduleResp>

    //获取配置清单
    @GET("conf/get-conf")
    suspend fun getConf(): BaseResponse<ConfResp>

    //获取倒计时
    @GET("conf/get-countdown")
    suspend fun getCountDown(): BaseResponse<CountDownResp>

    //获取通知
    @GET("conf/get-notice")
    suspend fun getNotice(): BaseResponse<NoticeResp>

    //获取关于
    @GET("conf/get-about")
    suspend fun getAbout(): BaseResponse<AboutResp>

    //获取学期
    @GET("/conf/get-semester")
    suspend fun getSemesterList(
        @Query("stu_num") stuNumber: String,
    ): BaseResponse<SemesterResp>

    //获取时间表
    @GET("conf/get-timetable")
    suspend fun getTimeTable(): BaseResponse<TimeTableResp>

}