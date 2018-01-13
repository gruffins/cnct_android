package com.ryanfung.cnct.contracts;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.models.Network;

import java.util.List;

public interface NewNetworkContract {

    interface View extends Contract.View {
        void onWifiEnabled(boolean enabled);
        void onNetworks(List<Network> networks);
        void onShowProgress();
        void onCreateSuccess();
        void onCreateError();
    }

    interface Presenter extends Contract.Presenter {
        void attachListeners();
        void startScanning();
        void stopScanning();
        void createNetwork(@NonNull Network network, @NonNull String password, boolean authorization, int distance);
    }

}
