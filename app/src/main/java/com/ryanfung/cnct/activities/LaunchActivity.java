package com.ryanfung.cnct.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.ryanfung.cnct.contracts.LaunchContract;
import com.ryanfung.cnct.presenters.LaunchPresenter;

import static com.ryanfung.cnct.utils.AndroidUtil.hasPermission;

public class LaunchActivity extends Activity implements LaunchContract.View {

    private LaunchContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPresenter(new LaunchPresenter(getPreferences(), getApi(), this));

        presenter.checkState(hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.viewDetached();
    }

    public void setPresenter(LaunchContract.Presenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void onSetup() {
        startActivity(new Intent(this, SetupActivity.class));
        finish();
    }

    @Override
    public void onAuthed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onWelcome() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

}
