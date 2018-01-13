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
public class ScanResultsToNetworksTests {

    private ScanResultsToNetworks converter;

    @Before
    public void setup() {
        converter = new ScanResultsToNetworks();
    }

    @Test
    public void conversion() throws Exception {
        ScanResult result = mock(ScanResult.class);
        result.SSID = "ssid";

        assertThat(converter.apply(Collections.singletonList(result))).isNotEmpty();
    }
}
