package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.EditProfileContract;
import com.ryanfung.cnct.exceptions.ApiException;
import com.ryanfung.cnct.models.User;
import com.ryanfung.cnct.network.CnctApi;
import com.ryanfung.cnct.rx.BaseObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.HttpException;

public class EditProfilePresenter extends Presenter<EditProfileContract.View> implements EditProfileContract.Presenter {

    public EditProfilePresenter(
            @NonNull Preferences preferences,
            @NonNull CnctApi api,
            @NonNull EditProfileContract.View view) {
        super(preferences, api, view);
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void loadUser() {
        view.onUserLoaded(getPreferences().getCurrentUser());
    }

    @Override
    public void save(
            @Nullable String username,
            @Nullable String email,
            @Nullable String currentPassword,
            @Nullable String newPassword) {
        User user = getPreferences().getCurrentUser();

        if (user != null) {
            Map<String, String> params = new HashMap<>();

            if (!user.username.equals(username)) {
                params.put("user[username]", username);
            }

            if (!user.email.equals(email)) {
                params.put("user[email]", email);
            }

            if (!TextUtils.isEmpty(newPassword)) {
                params.put("user[password]", newPassword);
            }

            if (!TextUtils.isEmpty(currentPassword)) {
                params.put("user[current_password]", currentPassword);
            }

            if (params.isEmpty()) {
                view.onSaved();
            } else {
                addDisposable(getApi()
                        .updateUser(user.id, params)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new BaseObserver<User>() {
                            @Override
                            public void onNext(User user) {
                                getPreferences().setCurrentUser(user);
                                view.onSaved();
                            }

                            @Override
                            public void onError(Throwable tr) {
                                if (tr instanceof HttpException) {
                                    view.onSaveFailed(new ApiException((HttpException) tr).getErrors());
                                } else {
                                    view.onSaveFailed(new HashMap<String, List<String>>());
                                }
                            }
                        }));
            }
        }
    }

}
