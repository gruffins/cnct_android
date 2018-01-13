package com.ryanfung.cnct.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class User {

    @NonNull
    public String id = "";

    @Nullable
    public String email;

    @NonNull
    public String username = "";

    @Nullable
    public Integer networkCount;

    @Nullable
    public Integer connectionCount;

    public String getInitials() {
        return username.substring(0, Math.min(3, username.length())).toUpperCase();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && id.equals(((User) obj).id);
    }

}
