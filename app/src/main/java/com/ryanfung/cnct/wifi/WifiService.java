package com.ryanfung.cnct.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.ryanfung.cnct.rx.DedupeScanResults;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class WifiService {

    private static final long INTERVAL = 5_000;

    private static final String ALLOWED_SECURITY = "^.*?(WEP|WPA|WPA2).*$";

    private final IntentFilter scanResultsFilter =
            new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

    private final IntentFilter wifiStateChangeFilter =
            new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);

    private Context context;
    private WifiManager manager;
    private ReplaySubject<List<ScanResult>> scanSubject;
    private PublishSubject<Boolean> stateSubject;
    private Handler handler;
    private boolean scanning;

    WifiStateChangedReceiver wifiStateChangedReceiver;
    ScanResultsReceiver scanResultsReceiver;

    public WifiService(@NonNull Context context, @NonNull WifiManager manager) {
        this.context = context.getApplicationContext();
        this.manager = manager;
        this.scanSubject = ReplaySubject.createWithSize(1);
        this.stateSubject = PublishSubject.create();
        this.handler = new Handler(Looper.getMainLooper());
        this.wifiStateChangedReceiver = new WifiStateChangedReceiver(this);
        this.scanResultsReceiver = new ScanResultsReceiver(this);

        this.context.registerReceiver(wifiStateChangedReceiver, wifiStateChangeFilter);
    }

    // =============================================================================================
    // API
    // =============================================================================================

    public Subject<List<ScanResult>> getScanSubject() {
        return scanSubject;
    }

    public Subject<Boolean> getStateSubject() {
        return stateSubject;
    }

    public boolean startScanning() {
        if (scanning || !isWifiEnabled()) {
            return false;
        }

        scanning = true;
        getScanSubject().onNext(getFilteredScanResults());
        context.registerReceiver(scanResultsReceiver, scanResultsFilter);
        manager.startScan();

        return true;
    }

    public boolean stopScanning() {
        if (!scanning) {
            return false;
        }

        scanning = false;
        context.unregisterReceiver(scanResultsReceiver);

        return true;
    }

    public boolean isScanning() {
        return scanning;
    }

    public boolean isWifiEnabled() {
        return manager.isWifiEnabled();
    }

    public void destroy() {
        stopScanning();
        context.unregisterReceiver(wifiStateChangedReceiver);
    }

    // =============================================================================================
    // Internals
    // =============================================================================================

    private void resultsAvailable() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isScanning()) {
                    manager.startScan();
                }
            }
        }, INTERVAL);

        if (isScanning()) {
            getScanSubject().onNext(getFilteredScanResults());
        }
    }

    private void wifiStateChanged(Intent intent) {
        int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

        if (state == WifiManager.WIFI_STATE_ENABLED) {
            getStateSubject().onNext(true);
        } else if (state == WifiManager.WIFI_STATE_DISABLED) {
            getStateSubject().onNext(false);
        }
    }

    private List<ScanResult> getFilteredScanResults() {
        List<ScanResult> results = new LinkedList<>(manager.getScanResults());

        return Observable.fromArray(results.toArray(new ScanResult[results.size()]))
                .filter(new Predicate<ScanResult>() {
                    @Override
                    public boolean test(ScanResult scanResult) throws Exception {
                        return scanResult.capabilities.toUpperCase().matches(ALLOWED_SECURITY);
                    }
                })
                .toList()
                .blockingGet();
    }

    protected static class WifiStateChangedReceiver extends BroadcastReceiver {

        private WifiService service;

        WifiStateChangedReceiver(WifiService service) {
            this.service = service;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                return;
            }

            service.wifiStateChanged(intent);
        }
    }

    protected static class ScanResultsReceiver extends BroadcastReceiver {

        private WifiService service;

        ScanResultsReceiver(WifiService service) {
            this.service = service;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                return;
            }

            service.resultsAvailable();
        }
    }
}
