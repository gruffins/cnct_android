package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.WelcomeContract;
import com.ryanfung.cnct.exceptions.ApiException;
import com.ryanfung.cnct.models.AccessToken;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.models.User;
import com.ryanfung.cnct.network.CnctApi;
import com.ryanfung.cnct.rx.BaseObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

public class WelcomePresenter extends Presenter<WelcomeContract.View> implements WelcomeContract.Presenter {

    public WelcomePresenter(
            @NonNull Preferences preferences,
            @NonNull CnctApi api,
            @NonNull WelcomeContract.View view) {
        super(preferences, api, view);
    }

    @Override
    public void createAccount(
            @NonNull final String username,
            @NonNull final String email,
            @NonNull final String password) {
        AccessToken token = getPreferences().getClientAccessToken();

        if (token != null) {
            final String authorization = String.format(Locale.US, "Bearer %s", token.accessToken);

            addDisposable(getApi().createUser(authorization, username, email, password)
                    .flatMap(new Function<User, ObservableSource<AccessToken>>() {
                        @Override
                        public ObservableSource<AccessToken> apply(User user) throws Exception {
                            return getApi().authenticate(authorization, email, password);
                        }
                    })
                    .flatMap(new Function<AccessToken, ObservableSource<User>>() {
                        @Override
                        public ObservableSource<User> apply(AccessToken accessToken) throws Exception {
                            getPreferences().setAccessToken(accessToken);
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
                            view.onSuccess();
                        }

                        @Override
                        public void onError(Throwable tr) {
                            if (tr instanceof HttpException) {
                                view.onError(new ApiException((HttpException) tr).getErrors());
                            } else {
                                view.onError(new HashMap<String, List<String>>());
                            }
                        }
                    }));
        }
    }
}
