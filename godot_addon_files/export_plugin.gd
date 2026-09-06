@tool
extends EditorPlugin
# =============================================================
#  export_plugin.gd — يُسجّل إضافة GodotGPS ضمن تصدير أندرويد
#  ⚠️ ملاحظة صريحة: هذا الملف كُتب حسب توثيق Godot الرسمي لبنية
#  الإضافات v2 (4.2+)، لكن لم يُختبَر فعليًا هنا (لا بيئة أندرويد
#  كاملة في هذه البيئة). راجعه مقابل القالب الرسمي إن واجهت مشكلة:
#  https://github.com/m4gr3d/Godot-Android-Plugin-Template
# =============================================================

var export_plugin : AndroidExportPlugin

func _enter_tree() -> void:
	export_plugin = AndroidExportPlugin.new()
	add_export_plugin(export_plugin)

func _exit_tree() -> void:
	remove_export_plugin(export_plugin)
	export_plugin = null


class AndroidExportPlugin extends EditorExportPlugin:
	var _plugin_name := "GodotGPS"

	func _supports_platform(platform: EditorExportPlatform) -> bool:
		return platform is EditorExportPlatformAndroid

	func _get_android_libraries(platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
		return PackedStringArray(["GodotGPS/GodotGPS-release.aar"])

	func _get_android_dependencies(platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
		return PackedStringArray(["com.google.android.gms:play-services-location:21.3.0"])

	func _get_name() -> String:
		return _plugin_name
