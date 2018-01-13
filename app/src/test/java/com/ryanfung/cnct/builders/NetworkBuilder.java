package com.ryanfung.cnct.builders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.models.User;
import com.ryanfung.cnct.utils.MessageDigestUtil;

import java.util.UUID;

public class NetworkBuilder extends Builder<Network> {

    @Nullable
    public String id;

    @Nullable
    public User user;

    @NonNull
    public String ssid = UUID.randomUUID().toString();

    @NonNull
    public String ssidHash = MessageDigestUtil.sha256(ssid);

    @NonNull
    public String bssid = UUID.randomUUID().toString();

    public int signalStrength = 0;

    @Nullable
    public String passphrase;

    public NetworkBuilder() {}

    public NetworkBuilder id(@Nullable String id) {
        this.id = id;
        return this;
    }

    public NetworkBuilder user(@Nullable User user) {
        this.user = user;
        return this;
    }

    public NetworkBuilder ssid(@NonNull String ssid) {
        this.ssid = ssid;
        return this;
    }

    public NetworkBuilder ssidHash(@NonNull String ssidHash) {
        this.ssidHash = ssidHash;
        return this;
    }

    public NetworkBuilder bssid(@NonNull String bssid) {
        this.bssid = bssid;
        return this;
    }

    public NetworkBuilder signalStrength(int signalStrength) {
        if (signalStrength >= Network.MAX_LEVEL || signalStrength < 0) throw new IllegalArgumentException("Value out of range");

        this.signalStrength = signalStrength;
        return this;
    }

    public NetworkBuilder passphrase(@Nullable String passphrase) {
        this.passphrase = passphrase;
        return this;
    }

    @Override
    public Network build() {
        Network network = new Network();
        network.id = id;
        network.user = user;
        network.ssid = ssid;
        network.bssid = bssid;
        network.signalStrength = signalStrength;
        network.passphrase = passphrase;
        network.ssidHash = ssidHash;
        return network;
    }

    // =============================================================================================
    // Factories
    // =============================================================================================

    public static NetworkBuilder local() {
        return new NetworkBuilder()
                .signalStrength(0)
                .passphrase("passphrase");
    }

    public static NetworkBuilder remote() {
        return new NetworkBuilder()
                .id(UUID.randomUUID().toString())
                .user(new UserBuilder().build());
    }
}
