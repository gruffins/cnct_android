package com.ryanfung.cnct.dialogs;

import com.afollestad.materialdialogs.DialogAction;
import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.models.Network;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class NetworkDialogTests extends DialogTests {

    @Mock
    private NetworkDialog.Listener listener;
    private Network network;
    private NetworkDialog dialog;

    @Override
    public void setup() {
        super.setup();

        network = new NetworkBuilder().build();
        dialog = new NetworkDialog.Builder(getContext(), network, listener).build();
    }

    @Test
    public void show() {
        dialog.show();
    }

    @Test
    public void onPositiveCallsListener() {
        dialog.onClick(dialog, DialogAction.POSITIVE);
        verify(listener).onNetworkDialog(any(Network.class), anyString(), anyBoolean(), anyInt());
    }
}
