package com.ryanfung.cnct.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.adapters.NetworksAdapter;
import com.ryanfung.cnct.adapters.holders.NetworkViewHolder;
import com.ryanfung.cnct.contracts.MyNetworksContract;
import com.ryanfung.cnct.dialogs.NetworkDialog;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.presenters.MyNetworksPresenter;
import com.ryanfung.cnct.views.RecyclerTouchItemHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyNetworksFragment extends Fragment implements MyNetworksContract.View,
        NetworksAdapter.OnNetworkSelectedListener, NetworkDialog.Listener, RecyclerTouchItemHelper.Listener {

    @BindView(R.id.my_networks_recycler_view)
    RecyclerView recyclerView;

    private MyNetworksContract.Presenter presenter;
    private NetworksAdapter adapter;

    // =============================================================================================
    // Lifecycle
    // =============================================================================================

    public void setPresenter(MyNetworksContract.Presenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPresenter(new MyNetworksPresenter(getPreferences(), getApi(), this));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_networks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        adapter = new NetworksAdapter(getContext(), NetworkViewHolder.ViewType.EDIT, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback callback = new RecyclerTouchItemHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.loadNetworks();
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void onNetworksLoaded(List<Network> networks) {
        adapter.set(networks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNetworkRemoved(Network network) {
        int position = adapter.remove(network);

        if (position >= 0) {
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onNetworkUpdated(Network network) {
        int position = adapter.update(network);

        if (position >= 0) {
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // =============================================================================================
    // NetworksAdapter.OnNetworkSelected
    // =============================================================================================

    @Override
    public void onNetworkSelected(@NonNull View view, @NonNull Network network) {
        new NetworkDialog.Builder(getContext(), network, this)
                .requirePassword(false)
                .positiveText(R.string.update)
                .build()
                .show();
    }

    // =============================================================================================
    // NetworkDialog.Listener
    // =============================================================================================

    @Override
    public void onNetworkDialog(Network network, String password, boolean authorization, int distance) {
        presenter.updateNetwork(network, password, authorization, distance);
    }

    // =============================================================================================
    // RecyclerTouchItemHelper.Listener
    // =============================================================================================

    @Override
    public void onSwiped(RecyclerView.ViewHolder holder, int direction, int position) {
        Network network = adapter.getItemAt(position);

        if (network != null) {
            presenter.removeNetwork(network);
        }
    }

    // =============================================================================================
    // Static
    // =============================================================================================

    public static MyNetworksFragment newInstance() {
        return new MyNetworksFragment();
    }

}
