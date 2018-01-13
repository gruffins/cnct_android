package com.ryanfung.cnct.content;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ryanfung.cnct.models.AccessToken;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.models.User;
import com.ryanfung.cnct.utils.RSA;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Preferences {

    private static final String CLIENT_ACCESS_TOKEN = "client_access_token";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String PRIVATE_KEY = "private_key";
    private static final String PUBLIC_KEY = "public_key";
    private static final String CURRENT_USER = "current_user";
    private static final String LOCAL_NETWORKS = "local_networks";
    private static final String DEVICE = "device_uuid";

    private SharedPreferences preferences;
    private Gson gson;

    public Preferences(@NonNull SharedPreferences preferences) {
        this.preferences = preferences;
        this.gson = new Gson();
    }

    public void setClientAccessToken(@Nullable AccessToken accessToken) {
        if (accessToken != null) {
            set(CLIENT_ACCESS_TOKEN, gson.toJson(accessToken));
        } else {
            set(CLIENT_ACCESS_TOKEN, null);
        }
    }

    @Nullable
    public AccessToken getClientAccessToken() {
        String json = preferences.getString(CLIENT_ACCESS_TOKEN, null);

        if (!TextUtils.isEmpty(json)) {
            return gson.fromJson(json, AccessToken.class);
        }

        return null;
    }

    public void setAccessToken(@Nullable AccessToken accessToken) {
        if (accessToken != null) {
            set(ACCESS_TOKEN, gson.toJson(accessToken));
        } else {
            set(ACCESS_TOKEN, null);
        }
    }

    @Nullable
    public AccessToken getAccessToken() {
        String json = preferences.getString(ACCESS_TOKEN, null);

        if (!TextUtils.isEmpty(json)) {
            return gson.fromJson(json, AccessToken.class);
        }

        return null;
    }

    public void setPublicKey(@Nullable PublicKey publicKey) {
        if (publicKey != null) {
            set(PUBLIC_KEY, Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT));
        } else {
            set(PUBLIC_KEY, null);
        }
    }

    @Nullable
    public PublicKey getPublicKey() {
        return RSA.publicKey(preferences.getString(PUBLIC_KEY, null));
    }

    public void setPrivateKey(@Nullable PrivateKey privateKey) {
        if (privateKey != null) {
            set(PRIVATE_KEY, Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT));
        } else {
            set(PRIVATE_KEY, null);
        }
    }

    @Nullable
    public PrivateKey getPrivateKey() {
        return RSA.privateKey(preferences.getString(PRIVATE_KEY, null));
    }

    public void setCurrentUser(@Nullable User user) {
        if (user != null) {
            set(CURRENT_USER, gson.toJson(user));
        } else {
            set(CURRENT_USER, null);
        }
    }

    @Nullable
    public User getCurrentUser() {
        String json = preferences.getString(CURRENT_USER, null);

        if (!TextUtils.isEmpty(json)) {
            return gson.fromJson(json, User.class);
        }

        return null;
    }

    public void setLocalNetworks(@NonNull List<Network> networks) {
        set(LOCAL_NETWORKS, gson.toJson(networks));
    }

    public List<Network> getLocalNetworks() {
        String json = preferences.getString(LOCAL_NETWORKS, "[]");

        return gson.fromJson(json, new TypeToken<ArrayList<Network>>(){}.getType());
    }

    public void setDevice(@Nullable Device device) {
        if (device != null) {
            set(DEVICE, gson.toJson(device));
        } else {
            set(DEVICE, null);
        }
    }

    @Nullable
    public Device getDevice() {
        String json = preferences.getString(DEVICE, null);

        if (!TextUtils.isEmpty(json)) {
            return gson.fromJson(json, Device.class);
        }

        return null;
    }

    public void clearUser() {
        setAccessToken(null);
        setCurrentUser(null);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    // =============================================================================================
    // Internals
    // =============================================================================================

    private void set(@NonNull String key, @Nullable Object value) {
        SharedPreferences.Editor editor = preferences.edit();

        if (value == null) {
            editor.remove(key).apply();
        } else {
            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            }

            editor.apply();
        }
    }
}
