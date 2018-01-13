package com.ryanfung.cnct.rx;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Function;

public class DedupeScanResults implements Function<List<ScanResult>, List<ScanResult>> {
    @Override
    public List<ScanResult> apply(List<ScanResult> scanResults) throws Exception {
        Map<String, ScanResult> map = new HashMap<>();

        for (ScanResult result : scanResults) {
            if (map.keySet().contains(result.SSID)) {
                if (result.level > map.get(result.SSID).level) {
                    map.put(result.SSID, result);
                }
            } else {
                map.put(result.SSID, result);
            }
        }

        List<ScanResult> results = new ArrayList<>(map.values());
        Collections.sort(results, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult left, ScanResult right) {
                return right.level - left.level;
            }
        });
        return results;
    }
}
