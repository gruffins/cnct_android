package com.ryanfung.cnct.utils;

import com.ryanfung.cnct.helpers.ConstructorHelper;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.google.common.truth.Truth.assertThat;

public class MessageDigestUtilTests {

    @Test(expected = InvocationTargetException.class)
    public void cannotConstruct() throws Exception {
        ConstructorHelper.invokeConstructor(MessageDigestUtil.class);
    }

    @Test
    public void sha256() {
        assertThat(MessageDigestUtil.sha256("test")).isNotEmpty();
    }
}
