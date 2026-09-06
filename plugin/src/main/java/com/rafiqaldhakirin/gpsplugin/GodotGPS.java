// =============================================================
//  GodotGPS.java  —  إضافة أندرويد أصلية لتحديد الموقع عبر GPS الحقيقي
//  (بنية Godot Android Plugin v2 — Godot 4.2+)
// =============================================================
//  الجزء البرمجي هنا يعمل مع device_location.gd الموجود أصلاً في
//  المشروع (يتحقق من وجود هذه الإضافة عبر Engine.has_singleton).
//  لم يُختبَر فعليًا على جهاز حقيقي بعد — تحقق منه ميدانيًا.
// =============================================================

package com.rafiqaldhakirin.gpsplugin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;

import java.util.HashSet;
import java.util.Set;

public class GodotGPS extends GodotPlugin {

    private FusedLocationProviderClient fusedClient;
    private LocationCallback locationCallback;
    private static final int PERMISSION_REQUEST_CODE = 4242;

    public GodotGPS(Godot godot) {
        super(godot);
        fusedClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public String getPluginName() {
        return "GodotGPS";
    }

    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = new HashSet<>();
        // إشارة واحدة: تُبعث عند الحصول على أول موقع دقيق
        signals.add(new SignalInfo("location_found", Double.class, Double.class));
        return signals;
    }

    // يُستدعى من GDScript: gps.request_permission()
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                             Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_CODE);
        }
    }

    // يُستدعى من GDScript: gps.start_location_updates()
    public void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // الصلاحية غير مُمنوحة — لا نفعل شيئاً، الجانب GDScript سيرجع
            // للإنترنت بعد انتهاء مهلة الانتظار (8 ثوان) تلقائياً
            return;
        }

        LocationRequest request = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMaxUpdates(1) // نحتاج تحديثاً واحداً دقيقاً فقط، لا تتبعاً مستمراً
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                Location loc = result.getLastLocation();
                if (loc != null) {
                    emitSignal("location_found", loc.getLatitude(), loc.getLongitude());
                }
                fusedClient.removeLocationUpdates(locationCallback);
            }
        };

        try {
            fusedClient.requestLocationUpdates(request, locationCallback, null);
        } catch (SecurityException e) {
            // صلاحية مرفوضة فعليًا وقت التنفيذ — نتجاهل، والإنترنت سيكون البديل
        }
    }
}
