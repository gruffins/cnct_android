package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.MainContract;
import com.ryanfung.cnct.models.User;
import com.ryanfung.cnct.network.CnctApi;

public class MainPresenter extends Presenter<MainContract.View> implements MainContract.Presenter {

    public MainPresenter(
            @NonNull Preferences preferences,
            @NonNull CnctApi api,
            @NonNull MainContract.View view) {
        super(preferences, api, view);
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void loadUser() {
        User user = getPreferences().getCurrentUser();

        if (user != null) {
            view.onUserLoaded(user);
        }
    }

    @Override
    public void signOut() {
        getPreferences().clearUser();
        view.onSignedOut();
    }
}
