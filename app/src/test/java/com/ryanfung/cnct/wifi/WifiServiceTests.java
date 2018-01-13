package com.ryanfung.cnct.wifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;
import io.reactivex.observers.TestObserver;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class WifiServiceTests {

    @Mock
    private WifiManager wifiManager;

    @Mock
    private Context context;
    private WifiService service;

    @Before
    public void setup() {
        initMocks(this);

        when(context.getApplicationContext()).thenReturn(context);

        service = new WifiService(context, wifiManager);
    }

    @After
    public void teardown() {
        service.destroy();
    }

    @Test
    public void registersWifiStateChangeReceiver() {
        verify(context, times(1)).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void startScanningReturnsFalseIfScanning() {
        service.startScanning();
        assertThat(service.startScanning()).isFalse();
    }

    @Test
    public void startScanningReturnsFalseIfWifiDisabled() {
        when(wifiManager.isWifiEnabled()).thenReturn(false);
        assertThat(service.startScanning()).isFalse();
    }

    @Test
    public void startScanningRegistersReceivers() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        service.startScanning();
        verify(context, times(2)).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void startScanningSetsScanning() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        service.startScanning();
        assertThat(service.isScanning()).isTrue();
    }

    @Test
    public void startScanningStartsScan() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        service.startScanning();
        verify(wifiManager).startScan();
    }

    @Test
    public void stopScanReturnsFalseIfNotScanning() {
        assertThat(service.stopScanning()).isFalse();
    }

    @Test
    public void stopScanningUnregistersReceivers() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        service.startScanning();
        service.stopScanning();

        verify(context).unregisterReceiver(any(BroadcastReceiver.class));
    }

    @Test
    public void stopScanningSetsScanning() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        service.startScanning();
        service.stopScanning();

        assertThat(service.isScanning()).isFalse();
    }

    @Test
    public void isWifiEnabled() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);

        assertThat(service.isWifiEnabled()).isTrue();
    }

    @Test
    public void scanResultsFiltersOutNonCompatibleNetworkTypes() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        service.startScanning();

        TestObserver<List<ScanResult>> observer = new TestObserver<>();

        ScanResult result = mock(ScanResult.class);
        result.capabilities = "wps";

        when(wifiManager.getScanResults()).thenReturn(Collections.singletonList(result));
        service.getScanSubject().subscribe(observer);

        Intent intent = new Intent();
        intent.setAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        service.scanResultsReceiver.onReceive(context, intent);

        assertThat(observer.values().get(1)).isEmpty();
    }

    @Test
    public void scanResultsPostDelayedIfScanning() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        service.startScanning();

        Intent intent = new Intent();
        intent.setAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        service.scanResultsReceiver.onReceive(context, intent);

        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();

        verify(wifiManager, times(2)).startScan();
    }

    @Test
    public void scanResultsPublishesWifiNetworks() {
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        service.startScanning();

        TestObserver<List<ScanResult>> observer = new TestObserver<>();

        ScanResult result = mock(ScanResult.class);
        result.capabilities = "wpa";

        when(wifiManager.getScanResults()).thenReturn(Collections.singletonList(result));
        service.getScanSubject().subscribe(observer);

        Intent intent = new Intent();
        intent.setAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        service.scanResultsReceiver.onReceive(context, intent);

        assertThat(observer.values().get(1)).isNotEmpty();
    }

    @Test
    public void wifiStateChangedEnabled() {
        TestObserver<Boolean> observer = new TestObserver<>();
        service.getStateSubject().subscribe(observer);

        Intent intent = new Intent();
        intent.setAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intent.putExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_ENABLED);

        service.wifiStateChangedReceiver.onReceive(context, intent);

        assertThat(observer.values().get(0)).isTrue();
    }

    @Test
    public void wifiStateChangedDisabled() {
        TestObserver<Boolean> observer = new TestObserver<>();
        service.getStateSubject().subscribe(observer);

        Intent intent = new Intent();
        intent.setAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intent.putExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

        service.wifiStateChangedReceiver.onReceive(context, intent);

        assertThat(observer.values().get(0)).isFalse();
    }
}
