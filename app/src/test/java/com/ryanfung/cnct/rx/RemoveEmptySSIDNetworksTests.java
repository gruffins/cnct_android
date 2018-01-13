package com.ryanfung.cnct.rx;

import android.net.wifi.ScanResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class RemoveEmptySSIDNetworksTests {

    private RemoveEmptySSIDNetworks function;

    @Before
    public void setup() {
        function = new RemoveEmptySSIDNetworks();
    }

    @Test
    public void removeEmptySSID() throws Exception {
        ScanResult result = mock(ScanResult.class);
        result.SSID = null;

        assertThat(function.apply(Collections.singletonList(result))).isEmpty();
    }

    @Test
    public void allowFilledSSID() throws Exception {
        ScanResult result = mock(ScanResult.class);
        result.SSID = "ssid";

        assertThat(function.apply(Collections.singletonList(result))).isNotEmpty();
    }
}
