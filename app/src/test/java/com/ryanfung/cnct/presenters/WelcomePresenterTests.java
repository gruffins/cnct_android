package com.ryanfung.cnct.presenters;

import com.ryanfung.cnct.builders.AccessTokenBuilder;
import com.ryanfung.cnct.builders.DeviceBuilder;
import com.ryanfung.cnct.builders.UserBuilder;
import com.ryanfung.cnct.contracts.WelcomeContract;
import com.ryanfung.cnct.models.AccessToken;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.models.User;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WelcomePresenterTests extends PresenterTests {

    @Mock
    private WelcomeContract.View view;
    private WelcomePresenter presenter;

    @Override
    public void setup() {
        super.setup();

        presenter = new WelcomePresenter(preferences, api, view);

        when(preferences.getClientAccessToken()).thenReturn(new AccessTokenBuilder().build());
        when(preferences.getDevice()).thenReturn(new DeviceBuilder().build());
    }

    @After
    public void teardown() {
        presenter.viewDetached();
    }

    @Test
    public void createAccountSetsAccessToken() {
        User user = new UserBuilder().build();

        when(api.createUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(user));

        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(api.me())
                .thenReturn(Observable.just(user));

        when(api.createDevice(anyString()))
                .thenReturn(Observable.just(new DeviceBuilder().build()));

        presenter.createAccount("username", "email", "password");

        verify(preferences).setAccessToken(any(AccessToken.class));
    }

    @Test
    public void createAccountSetsCurrentUser() {
        User user = new UserBuilder().build();

        when(api.createUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(user));

        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(api.me())
                .thenReturn(Observable.just(user));

        when(api.createDevice(anyString()))
                .thenReturn(Observable.just(new DeviceBuilder().build()));

        presenter.createAccount("username", "email", "password");

        verify(preferences).setCurrentUser(any(User.class));
    }

    @Test
    public void createAccountCallsSuccess() {
        User user = new UserBuilder().build();

        when(api.createUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(user));

        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(api.me())
                .thenReturn(Observable.just(user));

        when(api.createDevice(anyString()))
                .thenReturn(Observable.just(new DeviceBuilder().build()));

        presenter.createAccount("username", "email", "password");

        verify(view).onSuccess();
    }

    @Test
    public void createAccountSetsDevice() {
        User user = new UserBuilder().build();

        when(api.createUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(user));

        when(api.authenticate(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(api.me())
                .thenReturn(Observable.just(user));

        when(api.createDevice(anyString()))
                .thenReturn(Observable.just(new DeviceBuilder().build()));

        presenter.createAccount("username", "email", "password");

        verify(preferences).setDevice(any(Device.class));
    }

    @Test
    public void createAccountErrorHttpException() {
        Response response = Response.error(400,
                ResponseBody.create(MediaType.parse("application/json"), "{}"));

        when(api.createUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.<User>error(new HttpException(response)));

        presenter.createAccount("username", "email", "password");

        verify(view).onError(anyMap());
    }

    @Test
    public void createAccountErrorException() {
        when(api.createUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.<User>error(new Exception()));

        presenter.createAccount("username", "email", "password");

        verify(view).onError(anyMap());
    }
}
