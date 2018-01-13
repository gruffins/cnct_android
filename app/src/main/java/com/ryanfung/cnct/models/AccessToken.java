package com.ryanfung.cnct.models;

import android.support.annotation.NonNull;

public class AccessToken {

    @NonNull
    public String accessToken = "";

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AccessToken && accessToken.equals(((AccessToken) obj).accessToken);
    }

}
