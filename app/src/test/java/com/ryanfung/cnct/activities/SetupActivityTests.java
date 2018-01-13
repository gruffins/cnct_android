package com.ryanfung.cnct.activities;

import android.Manifest;
import android.content.pm.PackageManager;

import com.ryanfung.cnct.contracts.SetupContract;
import com.ryanfung.cnct.exceptions.PermissionException;
import com.ryanfung.cnct.rx.RSAKeyPairGenerator;

import org.junit.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Shadows.shadowOf;

public class SetupActivityTests extends ActivityTests {

    @Mock
    private SetupContract.Presenter presenter;
    private SetupActivity activity;

    @Override
    public void setup() {
        super.setup();

        activity = spy(buildActivity(SetupActivity.class).get());
        activity.setPresenter(presenter);
    }

    @Test
    public void onCreateCallsSetup() {
        activity.onCreate(null);
        verify(presenter).setup(anyString(), any(RSAKeyPairGenerator.class), anyBoolean());
    }

    @Test
    public void successUnauthed() {
        activity.onCreate(null);
        activity.onSuccess();

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(WelcomeActivity.class.getName());
    }

    @Test
    public void successFinishesSelf() {
        activity.onCreate(null);
        activity.onSuccess();

        assertThat(activity.isFinishing()).isTrue();
    }

    @Test
    public void requestPermission() {
        activity.onCreate(null);
        activity.onRequestPermission();

        verify(activity).requestPermissions(any(String[].class), anyInt());
    }

    @Test
    public void onRequestPermissionsResultGranted() {
        activity.onCreate(null);
        activity.onRequestPermissionsResult(
                SetupActivity.REQUEST_CODE_COARSE_PERMISSION,
                new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                new int[] {PackageManager.PERMISSION_GRANTED });

        verify(presenter, atLeastOnce()).setup(anyString(), any(RSAKeyPairGenerator.class), anyBoolean());
    }

    @Test
    public void onRequestPermissionsResultDenied() {
        activity.onCreate(null);
        activity.onRequestPermissionsResult(
                SetupActivity.REQUEST_CODE_COARSE_PERMISSION,
                new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                new int[] {PackageManager.PERMISSION_DENIED });

        verify(activity).onFailed(any(PermissionException.class));
    }
}
