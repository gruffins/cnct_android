package com.ryanfung.cnct.utils;

import android.support.annotation.Nullable;

public class Util {

    private Util() {
        throw new IllegalAccessError("Cannot instanttiate");
    }

    @Nullable
    public static String coalesce(String... strings) {
        for (String string : strings) {
            if (string != null && string.length() > 0) {
                return string;
            }
        }

        return null;
    }

    @Nullable
    public static <T> T coalesce(T... objects) {
        for (T obj : objects) {
            if (obj != null) {
                return obj;
            }
        }

        return null;
    }

}
