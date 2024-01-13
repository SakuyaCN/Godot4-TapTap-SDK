package com.sakuya.godot_taptap.taptap

import android.app.Activity
import android.util.Log
import com.tapsdk.bootstrap.Callback
import com.tapsdk.bootstrap.TapBootstrap
import com.tapsdk.bootstrap.account.TDSUser
import com.tapsdk.bootstrap.exceptions.TapError
import com.tapsdk.moment.TapMoment
import com.tapsdk.tapad.CustomUser
import com.tapsdk.tapad.TapAdConfig
import com.tapsdk.tapad.TapAdCustomController
import com.tapsdk.tapad.TapAdLocation
import com.tapsdk.tapad.TapAdManager
import com.tapsdk.tapad.TapAdSdk
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

    fun adnInit(activity: Activity,mediaId:Long,mediaName:String,mediaKey:String){
        TapAdManager.get().requestPermissionIfNecessary(activity)
        val config = TapAdConfig.Builder()
            .withMediaId(mediaId) // 必选参数。为 TapADN 注册的媒体 ID
            .withMediaName(mediaName) // 必选参数。为 TapADN 注册的媒体名称
            .withMediaKey(mediaKey) // 必选参数。媒体密钥，可以在TapADN后台查看
            .withMediaVersion("1") // 必选参数。默认值 "1"
            .withTapClientId(clientId) // 可选参数。TapTap 开发者中心的游戏 Client ID
            .enableDebug(true) // 可选参数，是否打开 debug 调试信息输出：true 打开、false 关闭。默认 false 关闭
            .withGameChannel("TapTap") // 必选参数，渠道
            .withCustomController(object : TapAdCustomController() {

                // 开发者可以传入 oaid
                // 信通院 OAID 的相关采集——如何获取 OAID：
                // 1. 移动安全联盟官网 http://www.msa-alliance.cn/
                // 2. 信通院统一 SDK 下载 http://msa-alliance.cn/col.jsp?id=120
                override fun getDevOaid(): String {

                    return "aaa"
                }

                // 是否允许 SDK 主动获取 ANDROID_ID
                override fun isCanUseAndroidId(): Boolean {
                    return true
                }

            })
            .build()

        TapAdSdk.init(activity, config)
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