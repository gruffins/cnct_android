package com.ryanfung.cnct.models;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ryanfung.cnct.utils.MessageDigestUtil;

import static com.ryanfung.cnct.utils.Util.coalesce;

public class Network {

    public static final int MAX_LEVEL = 5;

    @Nullable
    public String id;

    @Nullable
    public User user;

    @NonNull
    public String ssid = "";

    @NonNull
    public String ssidHash = "";

    @NonNull
    public String bssid = "";

    public transient int signalStrength;

    @Nullable
    public String passphrase;

    public int maxDistance = 0;
    public boolean authorization;

    public void merge(@NonNull Network network) {
        if (equals(network)) {
            id = coalesce(id, network.id);
            ssid = coalesce(ssid, network.ssid);
            ssidHash = coalesce(ssidHash, network.ssidHash);
            bssid = coalesce(bssid, network.bssid);
            signalStrength = coalesce(signalStrength, network.signalStrength);
            passphrase = coalesce(passphrase, network.passphrase);
            authorization = coalesce(authorization, network.authorization);
            maxDistance = coalesce(maxDistance, network.maxDistance);
        }
    }

    public boolean isConnected() {
        return false;
    }

    // =============================================================================================
    // Equality
    // =============================================================================================

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Network &&
                (ssid.equals(((Network) obj).ssid) || ssidHash.equals(((Network) obj).ssidHash));
    }

    @Override
    public int hashCode() {
        return ssid.hashCode();
    }

    // =============================================================================================
    // Statics
    // =============================================================================================

    public static Network from(ScanResult result) {
        Network network = new Network();
        network.ssid = result.SSID;
        network.ssidHash = MessageDigestUtil.sha256(result.SSID);
        network.bssid = result.BSSID;
        network.signalStrength = WifiManager.calculateSignalLevel(result.level, MAX_LEVEL);
        return network;
    }

}
