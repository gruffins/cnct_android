package com.ryanfung.cnct.contracts;

import android.support.annotation.Nullable;

import com.ryanfung.cnct.models.User;

import java.util.List;
import java.util.Map;

public interface EditProfileContract {

    interface View extends Contract.View {
        void onUserLoaded(User user);
        void onSaved();
        void onSaveFailed(Map<String, List<String>> errors);
    }

    interface Presenter extends Contract.Presenter {
        void loadUser();

        void save(
                @Nullable String username,
                @Nullable String email,
                @Nullable String currentPassword,
                @Nullable String newPassword);
    }

}
