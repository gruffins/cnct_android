package com.ryanfung.cnct.rx;

import android.net.wifi.ScanResult;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

public class DedupeScanResultsTests {

    @Test
    public void dedupes() throws Exception {
        ScanResult one = mock(ScanResult.class);
        ScanResult two = mock(ScanResult.class);
        ScanResult three = mock(ScanResult.class);

        one.level = -1;
        two.level = 0;
        one.SSID = two.SSID = "ssid";
        three.SSID = "nah";

        List<ScanResult> output = new DedupeScanResults().apply(Arrays.asList(one, two, three));

        assertThat(output).hasSize(2);
    }
}
