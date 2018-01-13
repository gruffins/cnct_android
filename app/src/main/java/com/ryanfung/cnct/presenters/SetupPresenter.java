package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.SetupContract;
import com.ryanfung.cnct.models.AccessToken;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.network.CnctApi;
import com.ryanfung.cnct.rx.BaseObserver;
import com.ryanfung.cnct.rx.RSAKeyPairGenerator;

import java.security.KeyPair;
import java.util.Locale;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

public class SetupPresenter extends Presenter<SetupContract.View> implements SetupContract.Presenter {

    public SetupPresenter(
            @NonNull Preferences preferences,
            @NonNull CnctApi api,
            @NonNull SetupContract.View view) {
        super(preferences, api, view);
    }

    @Override
    public void setup(String authorization, RSAKeyPairGenerator generator, boolean hasPermission) {
        if (!hasPermission) {
            view.onRequestPermission();
        } else {
            if (getPreferences().getDevice() == null) {
                getPreferences().setDevice(Device.newDevice());
            }

            getPreferences().setPrivateKey(null);
            getPreferences().setPublicKey(null);

            addDisposable(generator.create()
                    .flatMap(new SyncClientAccessToken(getPreferences(), getApi(), authorization))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<AccessToken>() {
                        @Override
                        public void onNext(AccessToken clientAccessToken) {
                            getPreferences().setClientAccessToken(clientAccessToken);

                            view.onSuccess();
                        }

                        @Override
                        public void onError(Throwable tr) {
                            view.onFailed(tr);
                        }
                    }));
        }

    }

    // =============================================================================================
    // Actions
    // =============================================================================================

    private static class SyncClientAccessToken implements Function<KeyPair, ObservableSource<AccessToken>> {

        private Preferences preferences;
        private CnctApi api;
        private String authorization;

        SyncClientAccessToken(Preferences preferences, CnctApi api, String authorization) {
            this.preferences = preferences;
            this.api = api;
            this.authorization = authorization;
        }

        @Override
        public ObservableSource<AccessToken> apply(KeyPair keyPair) throws Exception {
            preferences.setPublicKey(keyPair.getPublic());
            preferences.setPrivateKey(keyPair.getPrivate());

            String basicAuth = String.format(Locale.US, "Basic %s", authorization);

            return api.clientCredentials(basicAuth).retry(5);
        }
    }

}
