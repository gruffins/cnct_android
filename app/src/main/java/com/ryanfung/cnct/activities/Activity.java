package com.ryanfung.cnct.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.ryanfung.cnct.app.Application;
import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.Contract;
import com.ryanfung.cnct.network.CnctApi;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.ryanfung.cnct.utils.AndroidUtil.canTarget;

public abstract class Activity extends AppCompatActivity implements Contract.View {

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
        return (Application) getApplication();
    }

}
