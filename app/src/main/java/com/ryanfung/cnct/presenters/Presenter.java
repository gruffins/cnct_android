package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.Contract;
import com.ryanfung.cnct.network.CnctApi;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class Presenter<T extends Contract.View> implements Contract.Presenter {

    protected T view;

    private Preferences preferences;
    private CnctApi api;
    private CompositeDisposable disposables;

    public Presenter(@NonNull Preferences preferences, @NonNull CnctApi api, @NonNull T view) {
        this.view = view;
        this.preferences = preferences;
        this.api = api;
        this.disposables = new CompositeDisposable();
    }

    public void viewDetached() {
        disposables.dispose();
    }

    // =============================================================================================
    // Helpers
    // =============================================================================================

    protected void addDisposable(@NonNull Disposable disposable) {
        disposables.add(disposable);
    }

    protected Preferences getPreferences() {
        return preferences;
    }

    protected CnctApi getApi() {
        return api;
    }

}
