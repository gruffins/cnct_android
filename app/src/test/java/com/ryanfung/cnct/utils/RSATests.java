package com.ryanfung.cnct.utils;

import android.util.Base64;

import com.ryanfung.cnct.helpers.ConstructorHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.InvocationTargetException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class RSATests {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @Before
    public void setup() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair pair = generator.genKeyPair();

        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
    }

    @Test(expected=InvocationTargetException.class)
    public void cannotConstruct() throws Exception {
        ConstructorHelper.invokeConstructor(RSA.class);
    }

    @Test
    public void publicKeyFromBase64() throws Exception {
        String base64 = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
        assertThat(RSA.publicKey(base64)).isEqualTo(publicKey);
    }

    @Test
    public void publicKeyFromNull() {
        assertThat(RSA.publicKey(null)).isNull();
    }

    @Test
    public void publicKeyFromInvalid() {
        assertThat(RSA.publicKey("a")).isNull();
    }

    @Test
    public void privateKeyFromBase64() {
        String base64 = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
        assertThat(RSA.privateKey(base64)).isEqualTo(privateKey);
    }

    @Test
    public void privateKeyFromNull() {
        assertThat(RSA.privateKey(null)).isNull();
    }

    @Test
    public void privateKeyFromInvalid() {
        assertThat(RSA.privateKey("a")).isNull();
    }

    @Test
    public void encrypt() {
        String encrypted = RSA.encrypt(publicKey, "test");
        assertThat(encrypted).isNotEmpty();
    }

    @Test
    public void encryptFailureReturnsNull() {
        assertThat(RSA.encrypt(null, null)).isNull();
    }

    @Test
    public void decrypt() {
        String encrypted = RSA.encrypt(publicKey, "test");
        String decrypted = RSA.decrypt(privateKey, encrypted);

        assertThat(decrypted).isEqualTo("test");
    }

    @Test
    public void decryptFailureReturnsNull() {
        assertThat(RSA.decrypt(null, null)).isNull();
    }

}
