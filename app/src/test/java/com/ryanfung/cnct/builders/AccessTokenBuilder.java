package com.ryanfung.cnct.builders;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.models.AccessToken;

import java.util.UUID;

public class AccessTokenBuilder extends Builder<AccessToken> {

    private String accessToken = UUID.randomUUID().toString();

    public AccessTokenBuilder() {

    }

    public AccessTokenBuilder accessToken(@NonNull String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public AccessToken build() {
        AccessToken token = new AccessToken();
        token.accessToken = accessToken;
        return token;
    }
}
