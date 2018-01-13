package com.ryanfung.cnct.fragments;

import android.os.Bundle;

import com.ryanfung.cnct.app.Application;
import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.network.CnctApi;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class Fragment extends android.support.v4.app.Fragment {

    private CompositeDisposable disposables;

    // =============================================================================================
    // Lifecycle
    // =============================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposables = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        disposables.dispose();
    }

    // =============================================================================================
    // Helpers
    // =============================================================================================

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    protected Preferences getPreferences() {
        return getApp().getPreferences();
    }

    protected CnctApi getApi() {
        return getApp().getApi();
    }

    private Application getApp() {
        return (Application) getActivity().getApplication();
    }
}
