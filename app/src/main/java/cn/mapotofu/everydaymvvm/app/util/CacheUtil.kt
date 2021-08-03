package cn.mapotofu.everydaymvvm.app.util

import cn.mapotofu.everydaymvvm.data.model.entity.ClientConf
import cn.mapotofu.everydaymvvm.data.model.entity.UserInfo
import com.tencent.mmkv.MMKV

object CacheUtil {
    //MMKV的ID名
    private const val ID_LOGIN_DATA = "Login_Data"
    private const val ID_CONF_DATA = "Conf_Data"

    //字段key名
    private const val STU_INFO = "stu-info"
    private const val IS_LOGIN = "is-login"

    private const val CLIENT_CONF = "client-conf"
    private const val IS_CONF_UPDATE = "is-conf-update"

    //学生信息
    fun getStuInfo(): UserInfo? {
        val kv = MMKV.mmkvWithID(ID_LOGIN_DATA)
        val infoStr = kv.decodeString(STU_INFO)
        return if (infoStr.isNullOrEmpty()) {
           null
        } else {
            MoshiUtil.fromJson(infoStr, UserInfo::class.java)
            //Gson().fromJson(userStr, UserInfo::class.java)
        }
    }
    fun setStuInfo(infoResponse: UserInfo?) {
        val kv = MMKV.mmkvWithID(ID_LOGIN_DATA)
        if (infoResponse == null) {
            kv.encode(STU_INFO, "")
            setIsLogin(false)
        } else {
            kv.encode(STU_INFO, MoshiUtil.toJson(infoResponse))
            //kv.encode("user", Gson().toJson(userResponse))
            setIsLogin(true)
        }
    }

    //是否已经登陆
    fun getIsLogin(): Boolean {
        val kv = MMKV.mmkvWithID(ID_LOGIN_DATA)
        return kv.decodeBool(IS_LOGIN, false)
    }
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID(ID_LOGIN_DATA)
        kv.encode(IS_LOGIN, isLogin)
    }

    //客户端配置
    fun getClientConf(): ClientConf? {
        val kv = MMKV.mmkvWithID(ID_CONF_DATA)
        val confStr = kv.decodeString(CLIENT_CONF)
        return if (confStr.isNullOrEmpty()) {
            null
        } else {
            MoshiUtil.fromJson(confStr, ClientConf::class.java)
        }
    }
    fun setClientConf(confResponse: ClientConf?) {
        val kv = MMKV.mmkvWithID(ID_CONF_DATA)
        if (confResponse == null) {
            kv.encode(CLIENT_CONF, "")
        } else {
            kv.encode(CLIENT_CONF, MoshiUtil.toJson(confResponse))
        }
    }

    //是否已经更新过配置
    fun getIsConfUpdate(): Boolean {
        val kv = MMKV.mmkvWithID(ID_CONF_DATA)
        return kv.decodeBool(IS_CONF_UPDATE, false)
    }
    fun setIsConfUpdate(isConfUpdate: Boolean) {
        val kv = MMKV.mmkvWithID(ID_CONF_DATA)
        kv.encode(IS_CONF_UPDATE, isConfUpdate)
    }
}