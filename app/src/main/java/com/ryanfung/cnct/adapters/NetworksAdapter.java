package com.ryanfung.cnct.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.adapters.holders.NetworkViewHolder;
import com.ryanfung.cnct.models.Network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NetworksAdapter extends RecyclerView.Adapter<NetworkViewHolder> {

    private NetworkViewHolder.ViewType viewType;
    private List<Network> items;
    private Context context;

    @Nullable
    private OnNetworkSelectedListener listener;

    public NetworksAdapter(
            @NonNull Context context,
            @NonNull NetworkViewHolder.ViewType viewType,
            @Nullable OnNetworkSelectedListener listener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.viewType = viewType;
        this.listener = listener;
    }

    // =============================================================================================
    // API
    // =============================================================================================

    public void set(Collection<Network> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public int remove(@NonNull Network network) {
        if (items.contains(network)) {
            int index = items.indexOf(network);
            items.remove(network);
            return index;
        }

        return -1;
    }

    public int update(@NonNull Network network) {
        if (items.contains(network)) {
            int index = items.indexOf(network);
            items.get(index).merge(network);
            return index;
        }

        return -1;
    }

    @Nullable
    public Network getItemAt(int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }

        return null;
    }

    public OnNetworkSelectedListener getOnNetworkSelectedListener() {
        return listener;
    }

    // =============================================================================================
    // RecyclerView
    // =============================================================================================

    @Override
    public NetworkViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_network, parent, false);
        return new NetworkViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(NetworkViewHolder holder, int position) {
        holder.bind(items.get(position), getOnNetworkSelectedListener());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // =============================================================================================
    // Listener
    // =============================================================================================

    public interface OnNetworkSelectedListener {
        void onNetworkSelected(@NonNull View view, @NonNull Network network);
    }

}
