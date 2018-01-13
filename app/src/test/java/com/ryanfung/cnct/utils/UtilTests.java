package com.ryanfung.cnct.utils;

import com.ryanfung.cnct.helpers.ConstructorHelper;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.google.common.truth.Truth.assertThat;
import static com.ryanfung.cnct.utils.Util.coalesce;

public class UtilTests {

    @Test(expected = InvocationTargetException.class)
    public void cannotConstruct() throws Exception {
        ConstructorHelper.invokeConstructor(Util.class);
    }

    @Test
    public void coalesceString() {
        String output = coalesce(null, "test");
        assertThat(output).isEqualTo("test");
    }

    @Test
    public void coalesceEmptyString() {
        String output = coalesce("", "test");
        assertThat(output).isEqualTo("test");
    }

    @Test
    public void coalesceGeneric() {
        Object output = coalesce(null, "test");
        assertThat(output).isEqualTo("test");
    }

    @Test
    public void coalesceAllNull() {
        Object output = coalesce(null, null);
        assertThat(output).isNull();
    }
}
