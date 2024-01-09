package com.sakuya.godot_taptap.taptap

import android.app.Activity
import android.util.Log
import com.tapsdk.bootstrap.Callback
import com.tapsdk.bootstrap.TapBootstrap
import com.tapsdk.bootstrap.account.TDSUser
import com.tapsdk.bootstrap.exceptions.TapError
import com.tapsdk.moment.TapMoment
import com.tapsdk.tapconnect.TapConnect
import com.taptap.sdk.Profile
import com.taptap.sdk.TapLoginHelper
import com.tds.common.entities.TapAntiAddictionConfig
import com.tds.common.entities.TapConfig
import com.tds.common.models.TapRegionType


class GodotTapTap(val clientId:String?,
                  val clientToken:String?,
                  val serverUrl: String?) {

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var clientId: String? = null
        var clientToken: String? = null
        var serverUrl: String? = null

        fun build() = GodotTapTap(clientId,clientToken,serverUrl)
    }

    /*
    初始化sdk
     */
    fun init(activity: Activity,block: () -> Unit){
        activity.runOnUiThread {

            // TapAntiAddicitonConfig 构造方法中参数为是否显示切换账号
            val tapAntiAddictionConfig = TapAntiAddictionConfig(true)

            val tdsConfig = TapConfig.Builder()
                .withAppContext(activity) // Context 上下文
                .withClientId(clientId) // 必须，开发者中心对应 Client ID
                .withClientToken(clientToken) // 必须，开发者中心对应 Client Token
                .withServerUrl(serverUrl) // 必须，开发者中心 > 你的游戏 > 游戏服务 > 基本信息 > 域名配置 > API
                .withRegionType(TapRegionType.CN) // TapRegionType.CN：中国大陆，TapRegionType.IO：其他国家或地区
                .withAntiAddictionConfig(tapAntiAddictionConfig)
                .build()
            TapBootstrap.init(activity, tdsConfig)

            block()
        }
    }


    /*
    是否登录
     */
    fun isLogin():Boolean{
        return null != TDSUser.currentUser()
    }

    fun login(activity: Activity,block:(String?) -> Unit){
        TDSUser.loginWithTapTap(activity, object : Callback<TDSUser> {
            override fun onSuccess(resultUser: TDSUser) {
                block(resultUser.toJSONInfo())
            }

            override fun onFail(error: TapError) {
                Log.e("tap_login error",error.message.toString())
                block(null)
            }
        }, "public_profile")
    }

    /*
    获取登录信息
     */
    fun getCurrentProfile():String?{
        return try {
            val profile: Profile = TapLoginHelper.getCurrentProfile()
            profile.toJsonString()
        }catch (e:Exception){
            null
        }
    }

    /**
     * 登出
     */
    fun logOut(){
        TDSUser.logOut()
    }

    /**
     * 设置悬浮窗口
     */
    fun setEntryVisible(visible:Boolean){
        TapConnect.setEntryVisible(visible)
    }

    /**
     * 打开内嵌动态
     */
    fun momentOpen(ori:Int){
        //使用指定屏幕方向
        TapMoment.open(ori)
    }
}