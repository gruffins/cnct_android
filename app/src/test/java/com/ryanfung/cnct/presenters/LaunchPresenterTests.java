package com.ryanfung.cnct.presenters;


import com.ryanfung.cnct.builders.AccessTokenBuilder;
import com.ryanfung.cnct.builders.DeviceBuilder;
import com.ryanfung.cnct.contracts.LaunchContract;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LaunchPresenterTests extends PresenterTests {

    @Mock
    private LaunchContract.View view;
    private LaunchPresenter presenter;

    @Override
    public void setup() {
        super.setup();

        presenter = new LaunchPresenter(preferences, api, view);
    }

    @After
    public void teardown() {
        presenter.viewDetached();
    }

    @Test
    public void callsSetupWithoutDeviceUuid() {
        when(preferences.getDevice()).thenReturn(null);
        presenter.checkState(true);
        verify(view).onSetup();
    }

    @Test
    public void callsSetupWithoutPermissions() {
        presenter.checkState(false);
        verify(view).onSetup();
    }

    @Test
    public void callSetupWithoutPublicKey() {
        doReturn(null).when(preferences).getPublicKey();

        presenter.checkState(true);
        verify(view).onSetup();
    }

    @Test
    public void callSetupWithoutPrivateKey() {
        doReturn(mock(PublicKey.class)).when(preferences).getPublicKey();
        doReturn(null).when(preferences).getPrivateKey();

        presenter.checkState(true);
        verify(view).onSetup();
    }

    @Test
    public void callSetupWithoutClientAccessToken() {
        doReturn(mock(PublicKey.class)).when(preferences).getPublicKey();
        doReturn(mock(PrivateKey.class)).when(preferences).getPrivateKey();
        doReturn(null).when(preferences).getClientAccessToken();

        presenter.checkState(true);
        verify(view).onSetup();
    }

    @Test
    public void callAuthedIfAuthed() {
        doReturn(mock(PublicKey.class)).when(preferences).getPublicKey();
        doReturn(mock(PrivateKey.class)).when(preferences).getPrivateKey();
        doReturn(new AccessTokenBuilder().build()).when(preferences).getClientAccessToken();
        doReturn(new AccessTokenBuilder().build()).when(preferences).getAccessToken();
        doReturn(new DeviceBuilder().build()).when(preferences).getDevice();

        presenter.checkState(true);
        verify(view).onAuthed();
    }

    @Test
    public void callWelcome() {
        doReturn(mock(PublicKey.class)).when(preferences).getPublicKey();
        doReturn(mock(PrivateKey.class)).when(preferences).getPrivateKey();
        doReturn(new AccessTokenBuilder().build()).when(preferences).getClientAccessToken();
        doReturn(null).when(preferences).getAccessToken();
        doReturn(new DeviceBuilder().build()).when(preferences).getDevice();

        presenter.checkState(true);
        verify(view).onWelcome();
    }
}
