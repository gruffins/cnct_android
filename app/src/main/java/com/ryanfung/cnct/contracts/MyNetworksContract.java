package com.ryanfung.cnct.contracts;

import com.ryanfung.cnct.models.Network;

import java.util.List;

public interface MyNetworksContract {

    interface View extends Contract.View {
        void onNetworksLoaded(List<Network> networks);
        void onNetworkRemoved(Network network);
        void onNetworkUpdated(Network network);
        void onError(String message);
    }

    interface Presenter extends Contract.Presenter {
        void loadNetworks();
        void removeNetwork(Network network);
        void updateNetwork(Network network, String password, boolean authorization, int distance);
    }
}
