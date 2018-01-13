package com.ryanfung.cnct.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {

    private MessageDigestUtil() {
        throw new IllegalAccessError("Cannot instantiate");
    }

    @Nullable
    public static String sha256(@NonNull String input) {
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            byte[] data = digest.digest(input.getBytes("UTF-8"));
            return AndroidUtil.bytesToHex(data);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return null;
        }
    }

}
