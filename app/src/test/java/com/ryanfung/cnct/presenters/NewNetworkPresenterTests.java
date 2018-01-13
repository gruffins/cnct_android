package com.ryanfung.cnct.presenters;

import android.net.wifi.ScanResult;

import com.ryanfung.cnct.builders.DeviceBuilder;
import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.contracts.NewNetworkContract;
import com.ryanfung.cnct.wifi.WifiService;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewNetworkPresenterTests extends PresenterTests {

    @Mock
    private WifiService wifiService;

    @Mock
    private NewNetworkContract.View view;

    private NewNetworkPresenter presenter;
    private PublishSubject<List<ScanResult>> scanSubject;
    private PublishSubject<Boolean> stateSubject;

    @Override
    public void setup() {
        super.setup();

        presenter = spy(new NewNetworkPresenter(preferences, api, view, wifiService));

        scanSubject = PublishSubject.create();
        stateSubject = PublishSubject.create();

        when(wifiService.getScanSubject()).thenReturn(scanSubject);
        when(wifiService.getStateSubject()).thenReturn(stateSubject);
        when(preferences.getDevice()).thenReturn(new DeviceBuilder().build());
    }

    @Test
    public void attachListenersListensForNetworks() {
        ScanResult result = mock(ScanResult.class);
        result.SSID = "SSID";
        result.level = 0;
        result.capabilities = "[WPA2-PSK]";

        presenter.attachListeners();

        scanSubject.onNext(Collections.singletonList(result));

        verify(view).onNetworks(anyList());
    }

    @Test
    public void attachListenersListensForWifiEnabled() {
        presenter.attachListeners();

        stateSubject.onNext(true);

        verify(presenter).startScanning();
    }

    @Test
    public void attachListenersListensForWifiDisabled() {
        presenter.attachListeners();

        stateSubject.onNext(false);

        verify(presenter).stopScanning();
    }

    @Test
    public void startScanningCallsView() {
        presenter.startScanning();

        verify(view).onWifiEnabled(anyBoolean());
    }

    @Test
    public void startScanningCallsWifiService() {
        presenter.startScanning();

        verify(wifiService).startScanning();
    }

    @Test
    public void stopScanningCallsView() {
        presenter.stopScanning();

        verify(view).onWifiEnabled(anyBoolean());
    }

    @Test
    public void stopScanningCallsWifiService() {
        presenter.stopScanning();

        verify(wifiService).stopScanning();
    }

    @Test
    public void createNetworkSuccessCallsView() {
        when(api.createNetwork(anyMap()))
                .thenReturn(Observable.just(new NetworkBuilder().build()));

        presenter.createNetwork(new NetworkBuilder().build(), "password", true, 1);

        verify(view).onCreateSuccess();
    }

    @Test
    public void createNetworkSuccessAddsToLocalNetworks() {
        when(api.createNetwork(anyMap()))
                .thenReturn(Observable.just(new NetworkBuilder().build()));

        presenter.createNetwork(new NetworkBuilder().build(), "password", true, 1);

        verify(preferences).setLocalNetworks(anyList());
    }

    @Test
    public void createNetworkErrorCallsView() {
        when(api.createNetwork(anyMap()))
                .thenReturn(Observable.error(new Exception()));

        presenter.createNetwork(new NetworkBuilder().build(), "password", true, 1);

        verify(view).onCreateError();
    }
}
