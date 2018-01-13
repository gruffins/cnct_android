package com.ryanfung.cnct.presenters;

import com.google.common.collect.Lists;
import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.contracts.MyNetworksContract;
import com.ryanfung.cnct.models.Network;

import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Observable;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyNetworksPresenterTests extends PresenterTests {

    @Mock
    private MyNetworksContract.View view;

    private MyNetworksPresenter presenter;
    private Network network;

    @Override
    public void setup() {
        super.setup();

        presenter = new MyNetworksPresenter(preferences, api, view);
        network = new NetworkBuilder().build();
    }

    @Test
    public void loadNetworksCallsView() {
        presenter.loadNetworks();
        verify(view).onNetworksLoaded(anyList());
    }

    @Test
    public void removeNetworkSuccessRemovesFromLocalNetworks() {
        when(preferences.getLocalNetworks()).thenReturn(Lists.newArrayList(network));
        when(api.deleteNetwork(anyString())).thenReturn(Observable.just(network));

        presenter.removeNetwork(network);

        verify(preferences).setLocalNetworks(anyList());
    }

    @Test
    public void removeNetworkSuccessCallsView() {
        when(preferences.getLocalNetworks()).thenReturn(Lists.newArrayList(network));
        when(api.deleteNetwork(anyString())).thenReturn(Observable.just(network));

        presenter.removeNetwork(network);

        verify(view).onNetworkRemoved(eq(network));
    }

    @Test
    public void removeNetworkErrorCallsView() {
        when(api.deleteNetwork(anyString()))
                .thenReturn(Observable.<Network>error(new Exception()));

        presenter.removeNetwork(network);

        verify(view).onError(anyString());
    }

    @Test
    public void updateNetworkUpdatesLocalNetworks() {
        when(api.updateNetwork(anyString(), anyMap()))
                .thenReturn(Observable.just(network));

        presenter.updateNetwork(network, "password", true, 1);

        verify(preferences).setLocalNetworks(anyList());
    }

    @Test
    public void updateNetworkCallsView() {
        when(api.updateNetwork(anyString(), anyMap()))
                .thenReturn(Observable.just(network));

        presenter.updateNetwork(network, "password", true, 1);

        verify(view).onNetworkUpdated(eq(network));
    }

    @Test
    public void updateNetworkErrorCallsView() {
        when(api.updateNetwork(anyString(), anyMap()))
                .thenReturn(Observable.<Network>error(new Exception()));

        presenter.updateNetwork(network, "password", true, 1);

        verify(view).onError(anyString());
    }
}
