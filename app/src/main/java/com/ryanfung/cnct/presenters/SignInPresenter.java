package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.SignInContract;
import com.ryanfung.cnct.models.AccessToken;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.models.User;
import com.ryanfung.cnct.network.CnctApi;
import com.ryanfung.cnct.rx.BaseObserver;

import java.util.Locale;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SignInPresenter extends Presenter<SignInContract.View> implements SignInContract.Presenter {

    public SignInPresenter(
            @NonNull Preferences preferences,
            @NonNull CnctApi api,
            @NonNull SignInContract.View view) {
        super(preferences, api, view);
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void authenticate(String email, String password) {
        AccessToken clientToken = getPreferences().getClientAccessToken();

        if (clientToken != null) {
            String authorization = String.format(Locale.US, "Bearer %s", clientToken.accessToken);

            addDisposable(getApi()
                    .authenticate(authorization, email, password)
                    .flatMap(new Function<AccessToken, ObservableSource<User>>() {
                        @Override
                        public ObservableSource<User> apply(AccessToken token) throws Exception {
                            getPreferences().setAccessToken(token);
                            return getApi().me();
                        }
                    })
                    .doOnNext(new Consumer<User>() {
                        @Override
                        public void accept(User user) throws Exception {
                            addDisposable(getApi()
                                    .createDevice(getPreferences().getDevice().uuid)
                                    .subscribeWith(new BaseObserver<Device>() {
                                        @Override
                                        public void onNext(Device device) {
                                            getPreferences().setDevice(device);
                                        }
                                    }));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<User>() {
                        @Override
                        public void onNext(User user) {
                            getPreferences().setCurrentUser(user);
                            view.onAuthenticationSuccess();
                        }

                        @Override
                        public void onError(Throwable tr) {
                            view.onAuthenticationFailure();
                        }
                    }));

        }
    }
}
