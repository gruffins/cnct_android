package com.ryanfung.cnct.utils;

import com.google.gson.Gson;
import com.ryanfung.cnct.helpers.ConstructorHelper;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.google.common.truth.Truth.assertThat;

public class GsonUtilTests {

    @Test(expected=InvocationTargetException.class)
    public void cannotConstruct() throws Exception {
        ConstructorHelper.invokeConstructor(GsonUtil.class);
    }

    @Test
    public void defaultGson() {
        Gson gson = GsonUtil.defaultGson();
        assertThat(gson).isNotNull();
    }
}
