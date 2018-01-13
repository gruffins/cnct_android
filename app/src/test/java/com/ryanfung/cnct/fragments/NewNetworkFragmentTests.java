package com.ryanfung.cnct.fragments;

import android.view.View;

import com.google.common.collect.Lists;
import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.presenters.NewNetworkPresenter;

import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;


public class NewNetworkFragmentTests extends FragmentTests {

    @Mock
    private NewNetworkPresenter presenter;

    private NewNetworkFragment fragment;

    @Override
    public void setup() {
        super.setup();

        fragment = NewNetworkFragment.newInstance();
        fragment.setPresenter(presenter);

        startFragment(fragment);
    }

    @Test
    public void onCreateAttachesListeners() {
        verify(presenter).attachListeners();
    }

    @Test
    public void onResumeStartsScanning() {
        verify(presenter).startScanning();
    }

    @Test
    public void onPauseStopsScanning() {
        fragment.onPause();
        verify(presenter).stopScanning();
    }

    @Test
    public void onDestroy() {
        fragment.onDestroy();
        verify(presenter).viewDetached();
    }

    @Test
    public void onNetworkSelectedOpensDialog() {
        fragment.onNetworkSelected(new View(fragment.getContext()), new NetworkBuilder().build());
        assertThat(ShadowDialog.getLatestDialog()).isNotNull();
    }

    @Test
    public void wifiEnabledTrue() {
        fragment.onWifiEnabled(true);

        assertThat(fragment.wifiDisabledView.getVisibility()).isEqualTo(View.GONE);
        assertThat(fragment.recyclerView.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void wifiEnabledFalse() {
        fragment.onWifiEnabled(false);

        assertThat(fragment.wifiDisabledView.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(fragment.recyclerView.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void networks() {
        fragment.onNetworks(Lists.newArrayList(new NetworkBuilder().build()));
    }

    @Test
    public void showProgress() {
        fragment.onShowProgress();

        assertThat(ShadowDialog.getLatestDialog()).isNotNull();
    }

    @Test
    public void createSuccess() {
        fragment.onCreateSuccess();

        assertThat(fragment.getActivity().isFinishing()).isTrue();
    }

    @Test
    public void createError() {
        fragment.onCreateError();

        assertThat(ShadowToast.getLatestToast()).isNotNull();
    }

    @Test
    public void onCreateNetworkCallsPresenter() {
        fragment.onNetworkDialog(new NetworkBuilder().build(), "password", true, 1);
        verify(presenter).createNetwork(any(Network.class), anyString(), anyBoolean(), anyInt());
    }

}
