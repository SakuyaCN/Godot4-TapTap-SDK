extends Node2D


func _on_Button_pressed():
		GodotTapTap.tap_login()


func _on_button_3_pressed() -> void:
		GodotTapTap.momentOpen() 


func _on_button_2_pressed() -> void:
		GodotTapTap.quickCheck()
