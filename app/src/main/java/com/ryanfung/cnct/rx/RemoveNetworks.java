package com.ryanfung.cnct.rx;

import com.ryanfung.cnct.models.Network;

import java.util.List;

import io.reactivex.functions.Function;

public class RemoveNetworks implements Function<List<Network>, List<Network>> {

    private List<Network> networks;

    public RemoveNetworks(List<Network> networks) {
        this.networks = networks;
    }

    @Override
    public List<Network> apply(List<Network> networks) throws Exception {
        networks.removeAll(this.networks);
        return networks;
    }

}
