extends Node

var singleton #插件实例

signal onLoginResult(code,json)
signal onAntiAddictionCallback(code)
signal onTapMomentCallBack(code)
signal onRewardVideoAdCallBack(code)

func _ready():
	#初始化
	if Engine.has_singleton("GodotTapTapSDK"):
		singleton = Engine.get_singleton("GodotTapTapSDK")
		singleton.init("lwccao5wbryv5jvhfc","bSVA3KuDPr1eMvAcC4igiy4Ew8rUIlmffcDZmOon","https://sakuyaapi.zhangyongzhao.site")
		singleton.onLoginResult.connect(self._onLoginResult)
		singleton.onAntiAddictionCallback.connect(self._onAntiAddictionCallback)
		singleton.onTapMomentCallBack.connect(self._onTapMomentCallBack)
		singleton.onRewardVideoAdCallBack.connect(self._onRewardVideoAdCallBack)

#登录回调
func _onLoginResult(code,json):
		emit_signal("onLoginResult",code,json)

#防沉迷回调
func _onAntiAddictionCallback(code):
		emit_signal("onAntiAddictionCallback",code)

#内嵌动态回调
func _onTapMomentCallBack(code):
		emit_signal("onTapMomentCallBack",code)

#激励广告回调
func _onRewardVideoAdCallBack(code):
	emit_signal("onRewardVideoAdCallBack",code)

#调用登录接口
func tap_login():
		singleton.login()

#是否登录
func isLogin():
		return singleton.isLogin()

#获取当前登录信息
func getCurrentProfile():
		return singleton.getCurrentProfile()

#退出登录
func logOut():
		singleton.logOut()

#快速防沉迷认证
func quickCheck(id = null):
		if id == null:
			id = OS.get_unique_id()
		singleton.quickCheck(id)

#退出当前用户的防沉迷认证
func antiExit():
		singleton.antiExit()

#切换防沉迷环境
func setTestEnvironment(enable:bool):
		singleton.setTestEnvironment(enable)

#是否打开悬浮窗
func setEntryVisible(enable:bool):
		singleton.setEntryVisible(enable)

#打开内嵌动态
func momentOpen(ori = -1):
		singleton.momentOpen(ori)

#初始化广告sdk
func initAd(mediaId,mediaName,mediaKey):
	singleton.adnInit(mediaId,mediaName,mediaKey)

#加载激励广告
func initRewardVideoAd(spaceId,rewardName,extraInfo,userId):
	singleton.initRewardVideoAd(spaceId,rewardName,extraInfo,userId)

#展示激励广告
func showRewardVideoAd():
	singleton.showRewardVideoAd()
