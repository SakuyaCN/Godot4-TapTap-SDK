extends Node2D

func _ready():
	GodotTapTap.onRewardVideoAdCallBack.connect(self.onRewardVideoAdCallBack)

func onRewardVideoAdCallBack(code):
	print("===================")
	$Label.text = str(code)

func _on_Button_pressed():
		GodotTapTap.tap_login()


func _on_button_3_pressed() -> void:
		GodotTapTap.momentOpen() 


func _on_button_2_pressed() -> void:
		GodotTapTap.quickCheck()


func _on_button_4_pressed():
	GodotTapTap.initRewardVideoAd(1000774,"测试奖励","",OS.get_unique_id())


func _on_button_5_pressed():
	GodotTapTap.showRewardVideoAd()

#应用id
const mediaId = 0
#应用名称
const mediaName = ""
#key
const mediaKey = ""
func _on_button_6_pressed():
	GodotTapTap.initAd(mediaId,mediaName,mediaKey)
