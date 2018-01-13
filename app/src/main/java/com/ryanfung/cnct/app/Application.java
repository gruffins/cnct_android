package com.ryanfung.cnct.app;

import android.content.Context;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.network.ApiBuilder;
import com.ryanfung.cnct.network.CnctApi;

public class Application extends android.app.Application {

    private static final String SHARED_PREFERENCES = "cnct";

    private Preferences preferences;
    private CnctApi api;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = new Preferences(getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE));
        api = new ApiBuilder(preferences, getString(R.string.base_url)).build();
    }

    // =============================================================================================
    // API
    // =============================================================================================

    public Preferences getPreferences() {
        return preferences;
    }

    public CnctApi getApi() {
        return api;
    }
}
