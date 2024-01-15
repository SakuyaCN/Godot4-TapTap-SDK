@tool
extends EditorPlugin

# A class member to hold the editor export plugin during its lifecycle.
var export_plugin : AndroidExportPlugin

func _enter_tree():
	# Initialization of the plugin goes here.
	export_plugin = AndroidExportPlugin.new()
	add_export_plugin(export_plugin)
	add_autoload_singleton("GodotTapTap","res://addons/GodotTapTapSDK/GodotTapTap.gd")


func _exit_tree():
	# Clean-up of the plugin goes here.
	remove_export_plugin(export_plugin)
	export_plugin = null


class AndroidExportPlugin extends EditorExportPlugin:
	# TODO: Update to your plugin's name.
	var _plugin_name = "GodotTapTapSDK"

	var local_aar = [
		_plugin_name + "/bin/TapBootstrap_3.27.0.aar",
		_plugin_name + "/bin/TapCommon_3.27.0.aar",
		_plugin_name + "/bin/TapLogin_3.27.0.aar",
		_plugin_name + "/bin/AntiAddiction_3.27.0.aar",
		_plugin_name + "/bin/AntiAddictionUI_3.27.0.aar",
		_plugin_name + "/bin/TapConnect_3.27.0.aar",
		_plugin_name + "/bin/TapMoment_3.27.0.aar",
		_plugin_name + "/bin/TapAD_3.16.3.25h1.aar"
	]

	func _supports_platform(platform):
		if platform is EditorExportPlatformAndroid:
			return true
		return false

	func _get_android_libraries(platform, debug):
		var array = []
		if debug:
			array.append(_plugin_name + "/bin/debug/" + _plugin_name + "-debug.aar",)
		else:
			array.append(_plugin_name + "/bin/release/" + _plugin_name + "-release.aar")
		array.append_array(local_aar)
		return PackedStringArray(array)
		
	func _get_android_dependencies(platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
		return PackedStringArray(["cn.leancloud:storage-android:8.2.19","cn.leancloud:realtime-android:8.2.19",
				"com.squareup.okhttp3:okhttp:3.12.1","com.android.support:support-annotations:28.0.0","com.github.bumptech.glide:glide:4.9.0","com.android.support:appcompat-v7:28.0.0","com.android.support:support-v4:28.0.0","com.android.support:recyclerview-v7:28.0.0"])
		
	func _get_name():
		return _plugin_name
