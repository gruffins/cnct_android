package com.ryanfung.cnct.rx;


import android.net.wifi.ScanResult;

import com.ryanfung.cnct.models.Network;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class ScanResultsToNetworks implements Function<List<ScanResult>, List<Network>> {
    @Override
    public List<Network> apply(List<ScanResult> scanResults) throws Exception {
        ScanResult[] array = new ScanResult[scanResults.size()];
        return Observable.fromArray(scanResults.toArray(array))
                .map(new Function<ScanResult, Network>() {
                    @Override
                    public Network apply(ScanResult scanResult) throws Exception {
                        return Network.from(scanResult);
                    }
                })
                .toList()
                .blockingGet();
    }
}