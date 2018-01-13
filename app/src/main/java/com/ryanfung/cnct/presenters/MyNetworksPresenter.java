package com.ryanfung.cnct.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.contracts.MyNetworksContract;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.network.CnctApi;
import com.ryanfung.cnct.rx.BaseObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MyNetworksPresenter extends Presenter<MyNetworksContract.View> implements MyNetworksContract.Presenter {

    public MyNetworksPresenter(
            @NonNull Preferences preferences,
            @NonNull CnctApi api,
            @NonNull MyNetworksContract.View view) {
        super(preferences, api, view);
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void loadNetworks() {
        view.onNetworksLoaded(getPreferences().getLocalNetworks());
    }

    @Override
    public void removeNetwork(final Network network) {
        List<Network> networks = getPreferences().getLocalNetworks();
        networks.remove(network);
        getPreferences().setLocalNetworks(networks);

        view.onNetworkRemoved(network);

        addDisposable(getApi().deleteNetwork(network.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<Network>() {
                    @Override
                    public void onError(Throwable tr) {
                        view.onError(tr.getMessage());

                        List<Network> networks = getPreferences().getLocalNetworks();
                        networks.add(network);
                        getPreferences().setLocalNetworks(networks);

                        loadNetworks();
                    }
                }));
    }

    @Override
    public void updateNetwork(final Network network, @Nullable String password, boolean authorization, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("network[authorization]", String.valueOf(authorization));
        params.put("network[max_distance]", String.valueOf(distance));

        network.authorization = authorization;
        network.maxDistance = distance;

        if (!TextUtils.isEmpty(password)) {
            network.passphrase = password;
        }

        addDisposable(getApi().updateNetwork(network.id, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<Network>() {
                    @Override
                    public void onNext(Network updated) {
                        network.merge(updated);

                        List<Network> networks = getPreferences().getLocalNetworks();
                        networks.remove(network);
                        networks.add(network);
                        getPreferences().setLocalNetworks(networks);

                        view.onNetworkUpdated(network);
                    }

                    @Override
                    public void onError(Throwable tr) {
                        view.onError(tr.getMessage());
                    }
                }));
    }
}
