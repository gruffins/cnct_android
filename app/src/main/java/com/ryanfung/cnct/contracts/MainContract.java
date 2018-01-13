package com.ryanfung.cnct.contracts;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.models.User;

public interface MainContract {
    interface View extends Contract.View {
        void onUserLoaded(@NonNull User user);
        void onSignedOut();
    }

    interface Presenter extends Contract.Presenter {
        void loadUser();
        void signOut();
    }
}
