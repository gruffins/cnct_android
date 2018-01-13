package com.ryanfung.cnct.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.models.AccessToken;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

import static java.util.Locale.US;

public class Interceptor implements okhttp3.Interceptor {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private Preferences preferences;

    public Interceptor(@NonNull Preferences preferences) {
        this.preferences = preferences;
    }

    // =============================================================================================
    // Interceptor
    // =============================================================================================

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        AccessToken accessToken = preferences.getAccessToken();

        if (accessToken != null && TextUtils.isEmpty(chain.request().header(HEADER_AUTHORIZATION))) {
            builder.addHeader(HEADER_AUTHORIZATION,
                    String.format(US, "Bearer %s", accessToken.accessToken));
        }

        return chain.proceed(builder.build());
    }
}
