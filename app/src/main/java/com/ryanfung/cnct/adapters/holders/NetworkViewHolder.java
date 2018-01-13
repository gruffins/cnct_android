package com.ryanfung.cnct.adapters.holders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.adapters.NetworksAdapter;
import com.ryanfung.cnct.models.Network;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public enum ViewType {
        ADD,
        EDIT,
        CONNECT;

        void setTitle(AppCompatTextView titleView, Network network) {
            titleView.setText(network.ssid);
        }

        void setSubtitle(AppCompatTextView subtitleView, Network network) {
            Context context = subtitleView.getContext();

            switch (this) {
                case EDIT:
                    StringBuilder builder = new StringBuilder();

                    if (network.authorization) {
                        builder
                                .append(context.getString(R.string.requires_authorization))
                                .append(" | ");

                    }

                    builder.append(context
                            .getResources()
                            .getStringArray(R.array.add_network_distance_array)[network.maxDistance - 1]);

                    subtitleView.setText(builder.toString());
                    break;
                default:
                    subtitleView.setText(network.isConnected() ? R.string.connected : R.string.empty);
                    break;
            }
        }

        void setViewVisibility(AppCompatImageView imageView, AppCompatTextView titleView, AppCompatTextView subtitleView) {
            switch (this) {
                case EDIT:
                    imageView.setVisibility(View.GONE);
                    titleView.setVisibility(View.VISIBLE);
                    subtitleView.setVisibility(View.VISIBLE);
                    break;
                default:
                    imageView.setVisibility(View.VISIBLE);
                    titleView.setVisibility(View.VISIBLE);
                    subtitleView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @BindView(R.id.icon)
    AppCompatImageView imageView;

    @BindView(R.id.title)
    AppCompatTextView titleTextView;

    @BindView(R.id.subtitle)
    AppCompatTextView subtitleTextView;

    private ViewType viewType;
    private NetworksAdapter.OnNetworkSelectedListener listener;
    private Network network;

    public NetworkViewHolder(View itemView, ViewType viewType) {
        super(itemView);
        this.viewType = viewType;

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
    }

    public void bind(@NonNull Network network, @Nullable NetworksAdapter.OnNetworkSelectedListener listener) {
        this.network = network;
        this.listener = listener;

        viewType.setViewVisibility(imageView, titleTextView, subtitleTextView);
        viewType.setTitle(titleTextView, network);
        viewType.setSubtitle(subtitleTextView, network);

        setSignalStrength();

        subtitleTextView.setVisibility(TextUtils.isEmpty(subtitleTextView.getText()) ?
                View.GONE :
                View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onNetworkSelected(view, network);
        }
    }

    // =============================================================================================
    // Internals
    // =============================================================================================

    private void setSignalStrength() {
        switch (network.signalStrength) {
            case 0:
                imageView.setImageResource(R.drawable.ic_signal_wifi_0_bar_black_24dp);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_signal_wifi_1_bar_black_24dp);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_signal_wifi_2_bar_black_24dp);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_signal_wifi_3_bar_black_24dp);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ic_signal_wifi_4_bar_black_24dp);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_signal_wifi_0_bar_black_24dp);
                break;
        }
    }

}
