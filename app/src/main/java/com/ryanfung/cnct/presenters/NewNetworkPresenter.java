package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.NewNetworkContract;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.network.CnctApi;
import com.ryanfung.cnct.rx.BaseObserver;
import com.ryanfung.cnct.rx.DedupeScanResults;
import com.ryanfung.cnct.rx.RemoveEmptySSIDNetworks;
import com.ryanfung.cnct.rx.RemoveNetworks;
import com.ryanfung.cnct.rx.ScanResultsToNetworks;
import com.ryanfung.cnct.wifi.WifiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class NewNetworkPresenter extends Presenter<NewNetworkContract.View> implements NewNetworkContract.Presenter {

    private WifiService wifiService;

    public NewNetworkPresenter(
            @NonNull Preferences preferences,
            @NonNull CnctApi api,
            @NonNull NewNetworkContract.View view,
            @NonNull WifiService wifiService) {
        super(preferences, api, view);
        this.wifiService = wifiService;
    }

    @Override
    public void attachListeners() {
        addDisposable(wifiService
                .getScanSubject()
                .map(new RemoveEmptySSIDNetworks())
                .map(new DedupeScanResults())
                .map(new ScanResultsToNetworks())
                .map(new RemoveNetworks(getPreferences().getLocalNetworks()))
                .subscribe(new Consumer<List<Network>>() {
                    @Override
                    public void accept(List<Network> networks) throws Exception {
                        view.onNetworks(networks);
                    }
                }));

        addDisposable(wifiService
                .getStateSubject()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean enabled) throws Exception {
                        if (enabled) {
                            startScanning();
                        } else {
                            stopScanning();
                        }
                    }
                }));
    }

    @Override
    public void startScanning() {
        view.onWifiEnabled(wifiService.isWifiEnabled());
        wifiService.startScanning();
    }

    @Override
    public void stopScanning() {
        view.onWifiEnabled(wifiService.isWifiEnabled());
        wifiService.stopScanning();
    }

    @Override
    public void createNetwork(@NonNull final Network network, @NonNull final String password, boolean authorization, final int distance) {
        view.onShowProgress();

        network.passphrase = password;
        network.maxDistance = distance;

        Map<String, String> params = new HashMap<>();
        params.put("network[ssid_hash]", network.ssidHash);
        params.put("network[authorization]", String.valueOf(authorization));
        params.put("network[max_distance]", String.valueOf(distance));
        params.put("network[device_id]", getPreferences().getDevice().id);

        addDisposable(getApi().createNetwork(params)
                .doOnNext(new Consumer<Network>() {
                    @Override
                    public void accept(Network obj) throws Exception {
                        network.merge(obj);

                        List<Network> networks = getPreferences().getLocalNetworks();
                        networks.add(network);
                        getPreferences().setLocalNetworks(networks);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<Network>() {
                    @Override
                    public void onNext(Network network) {
                        view.onCreateSuccess();
                    }

                    @Override
                    public void onError(Throwable tr) {
                        view.onCreateError();
                    }
                }));
    }

}
