package com.ryanfung.cnct.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class AndroidUtil {

    private AndroidUtil() {
        throw new IllegalAccessError("Cannot instantiate");
    }

    public static String getMetaData(@NonNull Context context, @NonNull String key, String defaultValue) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            return bundle.getString(key, defaultValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static boolean canTarget(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static <T> T getService(@NonNull Context context, @NonNull String service) {
        return (T) context.getApplicationContext().getSystemService(service);
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String key) {
        if (canTarget(Build.VERSION_CODES.M)) {
            return context.checkSelfPermission(key) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        for (byte bite : bytes) {
            builder.append(String.format("%02x", bite));
        }

        return builder.toString();
    }

}
