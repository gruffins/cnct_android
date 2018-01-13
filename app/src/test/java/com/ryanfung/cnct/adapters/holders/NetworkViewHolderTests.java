package com.ryanfung.cnct.adapters.holders;


import android.view.LayoutInflater;
import android.view.View;

import com.google.common.collect.Lists;
import com.ryanfung.cnct.R;
import com.ryanfung.cnct.adapters.NetworksAdapter;
import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.models.Network;

import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class NetworkViewHolderTests extends ViewHolderTests {

    @Mock
    private NetworksAdapter.OnNetworkSelectedListener listener;

    private View view;
    private NetworkViewHolder holder;
    private Network network;

    @Override
    public void setup() {
        super.setup();

        network = new NetworkBuilder().build();
        view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_network, null);
        holder = new NetworkViewHolder(view, NetworkViewHolder.ViewType.ADD);
    }

    @Test
    public void bind() {
        holder.bind(network, listener);

        assertThat(holder.titleTextView.getText()).isEqualTo(network.ssid);
    }

    @Test
    public void onClick() {
        holder.bind(network, listener);
        holder.onClick(null);
        verify(listener).onNetworkSelected(any(View.class), any(Network.class));
    }

    @Test
    public void allNetworkStrengths() {
        List<Network> networks = Lists.newArrayList(
                new NetworkBuilder().signalStrength(0).build(),
                new NetworkBuilder().signalStrength(1).build(),
                new NetworkBuilder().signalStrength(2).build(),
                new NetworkBuilder().signalStrength(3).build(),
                new NetworkBuilder().signalStrength(4).build()
        );

        for (Network network : networks) {
            holder.bind(network, listener);
        }
    }

}
