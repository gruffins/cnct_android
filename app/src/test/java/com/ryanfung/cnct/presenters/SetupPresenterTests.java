package com.ryanfung.cnct.presenters;

import com.ryanfung.cnct.builders.AccessTokenBuilder;
import com.ryanfung.cnct.contracts.SetupContract;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.rx.RSAKeyPairGenerator;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import io.reactivex.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SetupPresenterTests extends PresenterTests {

    @Mock
    private SetupContract.View view;

    @Mock
    private RSAKeyPairGenerator generator;
    private SetupPresenter presenter;

    @Override
    public void setup() {
        super.setup();

        presenter = spy(new SetupPresenter(preferences, api, view));
    }

    @After
    public void teardown() {
        presenter.viewDetached();
    }

    @Test
    public void setupCallsRequestPermission() {
        presenter.setup("Authorization", generator, false);
        verify(view).onRequestPermission();
    }

    @Test
    public void setupFailureCallsFailed() {
        when(generator.create())
                .thenReturn(Observable.<KeyPair>error(new Exception()));

        presenter.setup("Authorization", generator, true);
        verify(view).onFailed(any(Throwable.class));
    }

    @Test
    public void setupSuccessAndAuthed() {
        KeyPair pair = new KeyPair(mock(PublicKey.class), mock(PrivateKey.class));

        when(generator.create())
                .thenReturn(Observable.just(pair));

        when(api.clientCredentials(anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        presenter.setup("Authorization", generator, true);
        verify(view).onSuccess();
    }

    @Test
    public void setupSetsDeviceUuid() {
        KeyPair pair = new KeyPair(mock(PublicKey.class), mock(PrivateKey.class));

        when(generator.create())
                .thenReturn(Observable.just(pair));

        when(api.clientCredentials(anyString()))
                .thenReturn(Observable.just(new AccessTokenBuilder().build()));

        when(preferences.getDevice()).thenReturn(null);

        presenter.setup("Authorization", generator, true);

        verify(preferences).setDevice(any(Device.class));
    }


}
