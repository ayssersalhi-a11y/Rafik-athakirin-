# إضافة GodotGPS — دليل البناء والدمج

## ما هذا المجلد؟
مشروع Gradle كامل + سير عمل GitHub Actions يُجمّعان إضافة أندرويد
حقيقية (GodotGPS) تمنح تطبيقك موقعًا عبر GPS الحقيقي، بلا حاجة لتثبيت
Android Studio على جهازك — GitHub تبني الملف نيابةً عنك.

⚠️ **هذا الجزء لم يُختبَر على جهاز حقيقي بعد** — الكود كُتب حسب
التوثيق الرسمي لـ Godot، لكن التحقق الميداني يبقى عليك.

---

## الخطوة 1 — أنشئ مستودع GitHub جديد
1. على github.com اضغط "New repository" (يمكن أن يكون خاصًا Private).
2. سمِّه مثلًا: `godot-gps-plugin`.

## الخطوة 2 — ارفع محتوى هذا المجلد
ارفع **محتوى** مجلد `android_plugin_source` (وليس المجلد نفسه) إلى
جذر المستودع الجديد، بحيث يكون الشكل:
```
godot-gps-plugin/
├── .github/workflows/build-plugin.yml
├── build.gradle
├── settings.gradle
├── plugin/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/rafiqaldhakirin/gpsplugin/GodotGPS.java
└── godot_addon_files/
    ├── plugin.cfg
    └── export_plugin.gd
```

## الخطوة 3 — شغّل البناء
بعد الرفع (push)، افتح تبويب **Actions** في المستودع — سيبدأ البناء
تلقائيًا (يأخذ 2-4 دقائق). إن لم يبدأ، اضغط "Run workflow" يدويًا.

## الخطوة 4 — حمّل الملف الناتج
بعد اكتمال البناء بنجاح (علامة ✅ خضراء)، افتح تفاصيل التشغيل، وفي
أسفل الصفحة ستجد **Artifacts** باسم `GodotGPS-aar` — حمّله وفُكّ ضغطه؛
بداخله ملف `GodotGPS-release.aar`.

## الخطوة 5 — ادمجه في مشروع Godot
1. داخل مشروع Godot، أنشئ المجلد:
   `res://android/plugins/GodotGPS/`
2. ضع بداخله ملف `GodotGPS-release.aar` الذي حمّلته.
3. انسخ `godot_addon_files/plugin.cfg` و`export_plugin.gd` إلى:
   `res://addons/GodotGPS/` (أنشئ المجلد إن لم يكن موجودًا).
4. من محرر Godot: **Project → Project Settings → Plugins** — فعّل
   إضافة GodotGPS من القائمة.
5. من **Project → Export → Android → Plugins** تأكد أن GodotGPS
   ظاهرة ومفعَّلة.
6. صدّر التطبيق كالمعتاد — الكود الموجود أصلًا في `device_location.gd`
   سيكتشف الإضافة تلقائيًا (`Engine.has_singleton("GodotGPS")`)
   ويستخدمها قبل اللجوء للإنترنت.

## إذا واجهت مشكلة
الخطوة 5 (خصوصًا ملف `export_plugin.gd`) هي الأكثر حساسية لتغيّرات
إصدار Godot. إن ظهرت أخطاء عند التصدير، قارن الملف مع القالب الرسمي:
https://github.com/m4gr3d/Godot-Android-Plugin-Template
