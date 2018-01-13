package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.LaunchContract;
import com.ryanfung.cnct.network.CnctApi;

public class LaunchPresenter extends Presenter<LaunchContract.View> implements LaunchContract.Presenter {

    public LaunchPresenter(
            @NonNull Preferences preferences,
            @NonNull CnctApi api,
            @NonNull LaunchContract.View view) {
        super(preferences, api, view);
    }

    @Override
    public void checkState(boolean hasPermission) {
        if (!hasPermission ||
                getPreferences().getPublicKey() == null ||
                getPreferences().getPrivateKey() == null ||
                getPreferences().getClientAccessToken() == null ||
                getPreferences().getDevice() == null) {
            view.onSetup();
        } else if (getPreferences().getAccessToken() != null) {
            view.onAuthed();
        } else {
            view.onWelcome();
        }
    }
}
