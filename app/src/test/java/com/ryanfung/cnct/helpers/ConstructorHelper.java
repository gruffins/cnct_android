package com.ryanfung.cnct.helpers;

import java.lang.reflect.Constructor;

public class ConstructorHelper {

    private ConstructorHelper() {

    }

    public static void invokeConstructor(Class<?> clazz, Object... args) throws Exception {
        Constructor<?> cstr = clazz.getDeclaredConstructor();
        cstr.setAccessible(true);
        cstr.newInstance(args);
    }
}
