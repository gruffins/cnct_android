package com.ryanfung.cnct.adapters;

import android.widget.FrameLayout;

import com.ryanfung.cnct.adapters.holders.NetworkViewHolder;
import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.models.Network;

import org.junit.Test;
import org.mockito.Mock;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NetworksAdapterTests extends AdapterTests {

    @Mock
    private NetworksAdapter.OnNetworkSelectedListener listener;
    private NetworksAdapter adapter;

    @Override
    public void setup() {
        super.setup();

        adapter = new NetworksAdapter(getContext(), NetworkViewHolder.ViewType.ADD, listener);
    }

    @Test
    public void set() {
        adapter.set(Collections.singletonList(new NetworkBuilder().build()));
        assertThat(adapter.getItemCount()).isEqualTo(1);
    }

    @Test
    public void removeValid() {
        Network network = new NetworkBuilder().build();
        adapter.set(Collections.singletonList(network));

        assertThat(adapter.remove(network)).isEqualTo(0);
    }

    @Test
    public void removeInvalid() {
        assertThat(adapter.remove(new NetworkBuilder().build())).isEqualTo(-1);
    }

    @Test
    public void updateValid() {
        Network network = new NetworkBuilder().build();
        adapter.set(Collections.singletonList(network));

        assertThat(adapter.update(network)).isEqualTo(0);
    }

    @Test
    public void updateInvalid() {
        assertThat(adapter.update(new NetworkBuilder().build())).isEqualTo(-1);
    }

    @Test
    public void getOnNetworkSelectedListener() {
        assertThat(adapter.getOnNetworkSelectedListener()).isNotNull();
    }

    @Test
    public void onCreateViewHolder() {
        NetworkViewHolder holder = adapter.onCreateViewHolder(new FrameLayout(getContext()), 0);
        assertThat(holder).isNotNull();
    }

    @Test
    public void onBindViewHolder() {
        adapter.set(Collections.singletonList(new NetworkBuilder().build()));
        NetworkViewHolder holder = mock(NetworkViewHolder.class);

        adapter.onBindViewHolder(holder, 0);

        verify(holder).bind(any(Network.class), any(NetworksAdapter.OnNetworkSelectedListener.class));
    }

}
