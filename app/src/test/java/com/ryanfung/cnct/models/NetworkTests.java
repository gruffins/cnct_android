package com.ryanfung.cnct.models;

import android.net.wifi.ScanResult;

import com.ryanfung.cnct.builders.NetworkBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class NetworkTests {

    private Network network;

    @Before
    public void setup() {
        network = new NetworkBuilder().build();
    }

    @Test
    public void mergeWithSameNetwork() {
        Network other = new NetworkBuilder()
                .ssid(network.ssid)
                .ssidHash(network.ssidHash)
                .build();

        network.merge(other);

        assertThat(network.id).isEqualTo(other.id);
        assertThat(network.ssid).isEqualTo(other.ssid);
        assertThat(network.ssidHash).isEqualTo(other.ssidHash);
    }

    @Test
    public void mergeWithDifferentNetwork() {
        Network other = new NetworkBuilder().build();

        network.merge(other);

        assertThat(network.ssid).isNotEqualTo(other.ssid);
        assertThat(network.bssid).isNotEqualTo(other.bssid);
    }

    @Test
    public void equalsSameSSID() {
        Network other = new NetworkBuilder().ssid(network.ssid).build();

        assertThat(other).isEqualTo(network);
    }

    @Test
    public void equalsSameSSIDHash() {
        Network other = new NetworkBuilder().ssidHash(network.ssidHash).build();

        assertThat(other).isEqualTo(network);
    }

    @Test
    public void hashCodeIsSSID() {
        assertThat(network.hashCode()).isEqualTo(network.ssid.hashCode());
    }

    @Test
    public void staticCreationFromScanResult() {
        ScanResult result = mock(ScanResult.class);
        result.SSID = "ssid";
        result.BSSID = "bssid";
        result.level = 0;

        Network network = Network.from(result);

        assertThat(network.ssid).isEqualTo(result.SSID);
        assertThat(network.bssid).isEqualTo(result.BSSID);
        assertThat(network.signalStrength).isEqualTo(4);
    }
}
