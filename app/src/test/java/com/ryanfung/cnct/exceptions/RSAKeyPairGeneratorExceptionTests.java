package com.ryanfung.cnct.exceptions;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RSAKeyPairGeneratorExceptionTests {

    @Test
    public void wrapException() {
        assertThat(new RSAKeyPairGeneratorException(new Exception())).isNotNull();
    }

}
