package com.ryanfung.cnct.rx;

import android.net.wifi.ScanResult;
import android.text.TextUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class RemoveEmptySSIDNetworks implements Function<List<ScanResult>, List<ScanResult>> {
    @Override
    public List<ScanResult> apply(List<ScanResult> scanResults) throws Exception {
        ScanResult[] array = new ScanResult[scanResults.size()];
        return Observable.fromArray(scanResults.toArray(array))
                .filter(new Predicate<ScanResult>() {
                    @Override
                    public boolean test(ScanResult scanResult) throws Exception {
                        return !TextUtils.isEmpty(scanResult.SSID);
                    }
                })
                .toList()
                .blockingGet();
    }
}
