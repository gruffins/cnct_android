package com.ryanfung.cnct.models;

import com.ryanfung.cnct.builders.AccessTokenBuilder;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class AccessTokenTests {

    @Test
    public void accessTokenBuilder() {
        assertThat(new AccessTokenBuilder().build()).isNotNull();
    }

    @Test
    public void accessTokensWithSameTokenAreEqual() {
        AccessToken token1 = new AccessTokenBuilder().build();
        AccessToken token2 = new AccessTokenBuilder().accessToken(token1.accessToken).build();

        assertThat(token1).isEqualTo(token2);
    }
}
