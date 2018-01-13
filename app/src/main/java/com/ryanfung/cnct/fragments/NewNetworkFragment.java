package com.ryanfung.cnct.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ryanfung.cnct.R;
import com.ryanfung.cnct.adapters.NetworksAdapter;
import com.ryanfung.cnct.adapters.holders.NetworkViewHolder;
import com.ryanfung.cnct.contracts.NewNetworkContract;
import com.ryanfung.cnct.dialogs.NetworkDialog;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.presenters.NewNetworkPresenter;
import com.ryanfung.cnct.utils.AndroidUtil;
import com.ryanfung.cnct.wifi.WifiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewNetworkFragment extends Fragment implements NewNetworkContract.View, NetworksAdapter.OnNetworkSelectedListener, NetworkDialog.Listener {

    @BindView(R.id.new_network_wifi_disabled_view)
    AppCompatImageView wifiDisabledView;

    @BindView(R.id.new_network_recycler_view)
    RecyclerView recyclerView;

    private NewNetworkContract.Presenter presenter;
    private NetworksAdapter adapter;
    private MaterialDialog progressDialog;

    // =============================================================================================
    // Lifecycle
    // =============================================================================================

    public void setPresenter(NewNetworkContract.Presenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WifiManager manager = AndroidUtil.getService(getContext(), Context.WIFI_SERVICE);
        WifiService service = new WifiService(getContext(), manager);

        setPresenter(new NewNetworkPresenter(getPreferences(), getApi(), this, service));
        presenter.attachListeners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_network, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        adapter = new NetworksAdapter(getContext(), NetworkViewHolder.ViewType.ADD, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.startScanning();
    }

    @Override
    public void onPause() {
        super.onPause();

        presenter.stopScanning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.viewDetached();
    }

    // =============================================================================================
    // NetworksAdapter
    // =============================================================================================

    @Override
    public void onNetworkSelected(@NonNull View view, @NonNull final Network network) {
        new NetworkDialog.Builder(getContext(), network, this)
                .positiveText(R.string.create)
                .build()
                .show();
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void onWifiEnabled(boolean enabled) {
        wifiDisabledView.setVisibility(enabled ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(enabled ? View.VISIBLE : View.GONE);

        wifiDisabledView.animate()
                .setDuration(250)
                .alpha(enabled ? 0 : 1)
                .start();

        recyclerView.animate()
                .setDuration(250)
                .alpha(enabled ? 1 : 0)
                .start();
    }

    @Override
    public void onNetworks(List<Network> networks) {
        adapter.set(networks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onShowProgress() {
        progressDialog = new MaterialDialog.Builder(getContext())
                .content(R.string.add_network_creating)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    @Override
    public void onCreateSuccess() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onCreateError() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        Toast.makeText(getContext(), R.string.add_network_error, Toast.LENGTH_SHORT).show();
    }

    // =============================================================================================
    // NetworkDialog.Listener
    // =============================================================================================

    @Override
    public void onNetworkDialog(Network network, String password, boolean authorization, int distance) {
        presenter.createNetwork(network, password, authorization, distance);
    }

    // =============================================================================================
    // Creation
    // =============================================================================================

    public static NewNetworkFragment newInstance() {
        return new NewNetworkFragment();
    }


}
