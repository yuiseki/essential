package net.yuiseki.essential;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

public class EssentialPermission {
    static String TAG = "EssentialPermission";

    static boolean hasAllPermission(Activity activity) {
        return hasOverlayPermission(activity)
                && hasUsageStatsPermission(activity)
                && hasAccessibilityPermission(activity);
    }
    public static void requestPermission(Activity activity) {
        if (!hasOverlayPermission(activity)){
            Log.d(TAG, "requestOverlayPermission");
            requestOverlayPermission(activity);
            return;
        }
        if (!hasUsageStatsPermission(activity)){
            Log.d(TAG, "requestUsageStatsPermission");
            requestUsageStatsPermission(activity);
            return;
        }
        if (!hasAccessibilityPermission(activity)){
            Log.d(TAG, "requestAccessibilityPermission");
            requestAccessibilityPermission(activity);
        }
    }



    private static Boolean hasOverlayPermission(Activity activity){
        return Settings.canDrawOverlays(activity);
    }
    private static void requestOverlayPermission(Activity activity){
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:"+activity.getPackageName()));
        intent.setFlags(268435456);
        try {
            activity.startActivityForResult(intent, EssentialActivity.REQUEST_OVERLAY);
        } catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }


    private static Boolean hasUsageStatsPermission(Activity activity){
        AppOpsManager appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOp(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), activity.getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            return (activity.checkPermission("android.permission.PACKAGE_USAGE_STATS",
                    android.os.Process.myPid(), android.os.Process.myUid())
                    == PackageManager.PERMISSION_GRANTED);
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private static void requestUsageStatsPermission(Activity activity){
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        try {
            activity.startActivityForResult(intent, EssentialActivity.REQUEST_USAGE_STATS);
        } catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

    private static Boolean hasAccessibilityPermission(Activity activity){
        int accessibilityEnabled;
        try {
            accessibilityEnabled = Settings.Secure.getInt(activity.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            if (accessibilityEnabled!=1){
                Log.d(TAG, "accessibilityEnabled!=1");
                return false;
            }

            String settingValue = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue == null) {
                Log.d(TAG, "settingValue==null");
                return false;
            }

            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
            TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
            splitter.setString(settingValue);
            while (splitter.hasNext()) {
                String accessibilityService = splitter.next();
                Log.d(TAG, "Setting: " + accessibilityService);
                if (accessibilityService.contains("essential")){
                    Log.d(TAG, "We've found the correct setting - accessibility is switched on!");
                    return true;
                }
            }
            return false;
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
            return false;
        }
    }

    private static void requestAccessibilityPermission(Activity activity) {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        activity.startActivityForResult(intent, EssentialActivity.REQUEST_ACCESSIBILITY);
    }

}
