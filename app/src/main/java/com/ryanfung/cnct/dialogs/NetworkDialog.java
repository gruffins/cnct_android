package com.ryanfung.cnct.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.ryanfung.cnct.R;
import com.ryanfung.cnct.models.Network;

import io.reactivex.functions.Consumer;

public class NetworkDialog extends MaterialDialog implements MaterialDialog.SingleButtonCallback {

    private AppCompatEditText passwordEditText;
    private AppCompatCheckBox authorizationCheckBox;
    private AppCompatSpinner distanceSpinner;

    private Network network;
    private Listener listener;
    private boolean requirePassword = true;

    protected NetworkDialog(
            @NonNull MaterialDialog.Builder builder,
            @NonNull Network network,
            boolean requirePassword,
            @NonNull Listener listener) {
        super(builder);

        builder.onPositive(this);

        this.passwordEditText = getCustomView().findViewById(R.id.create_password);
        this.authorizationCheckBox = getCustomView().findViewById(R.id.create_require_authorization);
        this.distanceSpinner = getCustomView().findViewById(R.id.create_distance);

        this.network = network;
        this.listener = listener;
        this.requirePassword = requirePassword;
    }

    @Override
    public void show() {
        distanceSpinner.setAdapter(ArrayAdapter.createFromResource(
                getContext(),
                R.array.add_network_distance_array,
                android.R.layout.simple_spinner_dropdown_item));

        RxTextView.textChanges(passwordEditText)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence str) throws Exception {
                        getActionButton(DialogAction.POSITIVE).setEnabled(str.length() > 0);
                    }
                });

        authorizationCheckBox.setChecked(network.authorization);
        distanceSpinner.setSelection(Math.max(0, network.maxDistance - 1));

        getActionButton(DialogAction.POSITIVE).setEnabled(!requirePassword);

        super.show();
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        switch (which) {
            case POSITIVE:
                listener.onNetworkDialog(network,
                        passwordEditText.getText().toString(),
                        authorizationCheckBox.isChecked(),
                        distanceSpinner.getSelectedItemPosition() + 1);
                break;
            default:
                break;
        }
    }

    // =============================================================================================
    // Listener
    // =============================================================================================

    public interface Listener {
        void onNetworkDialog(Network network, String password, boolean authorization, int distance);
    }

    // =============================================================================================
    // Builder
    // =============================================================================================

    public static class Builder extends MaterialDialog.Builder {

        private Network network;
        private Listener listener;
        private boolean requirePassword = true;

        public Builder(Context context, Network network, Listener listener) {
            super(context);
            this.network = network;
            this.listener = listener;
        }

        public Builder requirePassword(boolean requirePassword) {
            this.requirePassword = requirePassword;
            return this;
        }

        @Override
        public NetworkDialog build() {
            title(network.ssid)
                    .customView(R.layout.dialog_create_network, true)
                    .negativeText(R.string.cancel);

            return new NetworkDialog(this, network, requirePassword, listener);

        }

    }
}
