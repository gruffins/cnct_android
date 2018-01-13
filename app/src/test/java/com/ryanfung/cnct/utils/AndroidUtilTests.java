package com.ryanfung.cnct.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.ryanfung.cnct.helpers.ConstructorHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.M;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AndroidUtilTests {

    @Test(expected=InvocationTargetException.class)
    public void cannotInstantiate() throws Exception {
        ConstructorHelper.invokeConstructor(AndroidUtil.class);
    }

    @Test
    public void getMetaDataSuccess() throws Exception {
        Context context = mock(Context.class);
        PackageManager packageManager = mock(PackageManager.class);
        ApplicationInfo info = mock(ApplicationInfo.class);
        Bundle bundle = new Bundle();

        bundle.putString("key", "value");

        when(context.getPackageManager()).thenReturn(packageManager);
        when(packageManager.getApplicationInfo(anyString(), anyInt())).thenReturn(info);
        info.metaData = bundle;

        assertThat(AndroidUtil.getMetaData(context, "key", null)).isEqualTo("value");
    }

    @Test
    public void getMetaDataException() {
        Context context = mock(Context.class);
        when(context.getPackageManager()).thenThrow(new RuntimeException(""));

        assertThat(AndroidUtil.getMetaData(context, "key", "default_value")).isEqualTo("default_value");
    }

    @Test
    @Config(sdk = KITKAT)
    public void canTarget() {
        assertThat(AndroidUtil.canTarget(KITKAT)).isTrue();
    }

    @Test
    @Config(sdk = KITKAT)
    public void cannotTarget() {
        assertThat(AndroidUtil.canTarget(M)).isFalse();
    }

    @Test
    public void getService() {
        assertThat(AndroidUtil.getService(RuntimeEnvironment.application, Context.WIFI_SERVICE))
                .isNotNull();
    }

    @Test
    @Config(sdk = M)
    public void hasPermissionAndroidM() {
        Context context = mock(Context.class);

        AndroidUtil.hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        verify(context).checkSelfPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    @Test
    @Config(sdk = KITKAT)
    public void hasPermission() {
        Context context = mock(Context.class);
        assertThat(AndroidUtil.hasPermission(context, "key")).isTrue();;
    }

}
