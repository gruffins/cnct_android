package com.ryanfung.cnct.presenters;

import com.ryanfung.cnct.builders.AccessTokenBuilder;
import com.ryanfung.cnct.builders.DeviceBuilder;
import com.ryanfung.cnct.builders.UserBuilder;
import com.ryanfung.cnct.contracts.SignInContract;
import com.ryanfung.cnct.models.AccessToken;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.models.User;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SignInPresenterTests extends PresenterTests {

    @Mock
    private SignInContract.View view;

    private SignInPresenter presenter;

    @Override
    public void setup() {
        super.setup();

        presenter = new SignInPresenter(preferences, api, view);

        when(preferences.getClientAccessToken()).thenReturn(new AccessTokenBuilder().build());
        when(preferences.getDevice()).thenReturn(new DeviceBuilder().build());
    }

    @After
    public void teardown() {
        presenter.viewDetached();
    }

    @Test
    public void successSavesAccessToken() {
        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(api.me())
                .thenReturn(Observable.just(UserBuilder.fullUser().build()));

        presenter.authenticate("email", "password");

        verify(preferences).setAccessToken(any(AccessToken.class));
    }

    @Test
    public void successSetsCurrentDevice() {
        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(api.me())
                .thenReturn(Observable.just(UserBuilder.fullUser().build()));

        when(api.createDevice(anyString()))
                .thenReturn(Observable.just(new DeviceBuilder().build()));

        presenter.authenticate("email", "password");

        verify(preferences).setDevice(any(Device.class));
    }

    @Test
    public void successSetsCurrentUser() {
        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(api.me())
                .thenReturn(Observable.just(UserBuilder.fullUser().build()));

        when(api.createDevice(anyString()))
                .thenReturn(Observable.just(new DeviceBuilder().build()));

        presenter.authenticate("email", "password");

        verify(preferences).setCurrentUser(any(User.class));
    }

    @Test
    public void successCallsView() {
        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(api.me())
                .thenReturn(Observable.just(UserBuilder.fullUser().build()));

        when(api.createDevice(anyString()))
                .thenReturn(Observable.just(new DeviceBuilder().build()));

        presenter.authenticate("email", "password");

        verify(view).onAuthenticationSuccess();
    }

    @Test
    public void errorCallsView() {
        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.<AccessToken>error(new Exception("")));

        presenter.authenticate("email", "password");

        verify(view).onAuthenticationFailure();
    }
}
