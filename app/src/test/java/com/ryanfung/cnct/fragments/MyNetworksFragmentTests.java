package com.ryanfung.cnct.fragments;

import android.view.View;

import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.presenters.MyNetworksPresenter;

import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

public class MyNetworksFragmentTests extends FragmentTests {

    @Mock
    private MyNetworksPresenter presenter;
    private MyNetworksFragment fragment;

    @Override
    public void setup() {
        super.setup();

        fragment = MyNetworksFragment.newInstance();
        fragment.setPresenter(presenter);

        startFragment(fragment);
    }

    @Test
    public void onStartLoadsNetworks() {
        verify(presenter).loadNetworks();
    }

    @Test
    public void onNetworksLoaded() {
        List<Network> networks = Collections.singletonList(new NetworkBuilder().build());

        fragment.onNetworksLoaded(networks);
    }

    @Test
    public void onNetworkRemoved() {
        Network network = new NetworkBuilder().build();
        List<Network> networks = Collections.singletonList(network);

        fragment.onNetworksLoaded(networks);
        fragment.onNetworkRemoved(network);
    }

    @Test
    public void onNetworkUpdated() {
        Network network = new NetworkBuilder().build();
        List<Network> networks = Collections.singletonList(network);

        fragment.onNetworksLoaded(networks);
        fragment.onNetworkUpdated(network);
    }

    @Test
    public void onError() {
        fragment.onError("error");

        assertThat(ShadowToast.getLatestToast()).isNotNull();
    }

    @Test
    public void onNetworkSelectedOpensDialog() {
        fragment.onNetworkSelected(
                new View(RuntimeEnvironment.application), new NetworkBuilder().build());

        assertThat(ShadowDialog.getLatestDialog()).isNotNull();
    }

    @Test
    public void onNetworkDialogCallsPresenter() {
        fragment.onNetworkDialog(new NetworkBuilder().build(), "", true, 1);

        verify(presenter).updateNetwork(any(Network.class), anyString(), anyBoolean(), anyInt());
    }

    @Test
    public void onSwipedCallsPresenter() {
        Network network = new NetworkBuilder().build();
        List<Network> networks = Collections.singletonList(network);

        fragment.onNetworksLoaded(networks);

        fragment.onSwiped(null, 0, 0);
        verify(presenter).removeNetwork(any(Network.class));
    }
}
