package com.ryanfung.cnct.rx;

import com.google.common.collect.Lists;
import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.models.Network;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class RemoveNetworksTests {


    @Test
    public void removeNetworks() throws Exception {
        Network remove = new NetworkBuilder().build();
        List<Network> networks = Lists.newArrayList(remove, new NetworkBuilder().build());

        List<Network> output = new RemoveNetworks(Collections.singletonList(remove)).apply(networks);

        assertThat(output).doesNotContain(remove);
    }
}
