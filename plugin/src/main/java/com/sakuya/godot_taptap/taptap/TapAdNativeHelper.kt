package com.sakuya.godot_taptap.taptap

import android.app.Activity
import com.tapsdk.tapad.AdRequest
import com.tapsdk.tapad.TapAdManager
import com.tapsdk.tapad.TapAdNative
import com.tapsdk.tapad.TapAdNative.RewardVideoAdListener
import com.tapsdk.tapad.TapRewardVideoAd
import com.tapsdk.tapad.TapRewardVideoAd.RewardAdInteractionListener


object TapAdNativeHelper {

    private var tapAdNative : TapAdNative ?= null
    private var tapRewardVideoAd: TapRewardVideoAd ?= null

    fun initAd(activity: Activity,spaceId:Int,rewardName:String,extraInfo:String,userId:String,block:(code:Int)-> Unit){
        tapAdNative = TapAdManager.get().createAdNative(activity)
        val adRequest: AdRequest = AdRequest.Builder()
            .withSpaceId(spaceId) // 广告后台获取广告位id
            .withRewordName(rewardName) // 奖品名称
            .withExtra1(extraInfo) // 游戏如果要通过 s2s 验证激励广告有效性的时候需要传入一些辅助信息来验证本次激励活动是否有效
            .withUserId(userId) // 游戏如果要通过 s2s 验证激励广告有效性的时候需要传入游戏中的玩家 id，来验证是否可以对当前用户发放奖励
            .build()
        tapAdNative?.loadRewardVideoAd(adRequest, object : RewardVideoAdListener {
            override fun onError(code: Int, message: String) {
                // 获取失败
                block(500)
            }

            override fun onRewardVideoAdLoad(rewardVideoAd: TapRewardVideoAd) {
                // 获取广告成功，可以展示广告
                block(200)
            }

            override fun onRewardVideoCached(rewardVideoAd: TapRewardVideoAd) {
                tapRewardVideoAd = rewardVideoAd
                tapRewardVideoAd?.setRewardAdInteractionListener(object : RewardAdInteractionListener {
                    override fun onAdShow() {
                        // 激励广告已显示
                        block(201)
                    }

                    override fun onAdClose() {
                        // 激励广告已经关闭
                        block(202)
                    }

                    override fun onVideoComplete() {
                        // 视频播放结束
                        block(203)
                    }

                    override fun onVideoError() {
                        // 视频出错
                        block(204)
                    }

                    override fun onRewardVerify(
                        rewardVerify: Boolean,
                        rewardAmount: Int,
                        rewardName: String,
                        code: Int,
                        msg: String
                    ) {
                        block(205)
                        // 激励任务已完成，游戏可以选择在此时进行玩家奖励，或者进一步通过 s2s 的流程来确认是否可以对当前玩家发放奖励
                    }

                    override fun onSkippedVideo() {
                        // 激励广告中玩家点击了跳过视频的按钮
                        block(206)
                    }

                    override fun onAdClick() {
                        // 激励广告点击事件
                        block(207)
                    }
                })
            }
        })
    }

    fun showAd(adtivity:Activity){
        tapRewardVideoAd?.showRewardVideoAd(adtivity)

    }

}