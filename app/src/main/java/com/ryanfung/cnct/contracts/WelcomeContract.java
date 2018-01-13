package com.ryanfung.cnct.contracts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

public interface WelcomeContract {

    interface View extends Contract.View {
        void onError(@Nullable Map<String, List<String>> errors);
        void onSuccess();
    }

    interface Presenter extends Contract.Presenter {
        void createAccount(@NonNull String username, @NonNull String email, @NonNull String password);
    }
}
