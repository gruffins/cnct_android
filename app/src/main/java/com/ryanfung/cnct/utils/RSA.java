package com.ryanfung.cnct.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {

    private static final String TAG = "RSA";
    private static final String RSA = "RSA";
    private static final String UTF8 = "UTF-8";

    private RSA() {
        throw new IllegalAccessError("Cannot instantiate");
    }

    // =============================================================================================
    // Key Management
    // =============================================================================================

    @Nullable
    public static PublicKey publicKey(@Nullable String base64) {
        if (base64 == null) {
            return null;
        }

        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            return KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(bytes));
        } catch (Exception ex) {
            Log.e(TAG, "Could not recover public key.", ex);
        }

        return null;
    }

    @Nullable
    public static PrivateKey privateKey(@Nullable String base64) {
        if (base64 == null) {
            return null;
        }

        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            return KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(bytes));
        } catch (Exception ex) {
            Log.e(TAG, "Could not recover private key.", ex);
        }

        return null;
    }

    // =============================================================================================
    // Encrypt / Decrypt
    // =============================================================================================

    @Nullable
    public static String encrypt(@NonNull PublicKey key, @NonNull String clearText) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] bytes = cipher.doFinal(clearText.getBytes(UTF8));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception ex) {
            Log.e(TAG, "Could not encrypt.", ex);
        }

        return null;
    }

    @Nullable
    public static String decrypt(@NonNull PrivateKey key, @NonNull String base64) {
        Cipher cipher;

        try {
            cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encrypted = Base64.decode(base64, Base64.DEFAULT);
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, UTF8);
        } catch (Exception ex) {
            Log.e(TAG, "Could not decrypt.", ex);
        }

        return null;
    }
}
