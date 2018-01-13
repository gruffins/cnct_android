package com.ryanfung.cnct.builders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ryanfung.cnct.models.User;

import java.util.UUID;

public class UserBuilder extends Builder<User> {

    private String id = String.valueOf(UUID.randomUUID().hashCode());
    private String email;
    private String username = String.valueOf(UUID.randomUUID().hashCode());
    private Integer networkCount;
    private Integer connectionCount;

    public UserBuilder() {

    }

    public UserBuilder id(@NonNull String id) {
        this.id = id;
        return this;
    }


    public UserBuilder email(@Nullable String email) {
        this.email = email;
        return this;
    }

    public UserBuilder username(@NonNull String username) {
        this.username = username;
        return this;
    }

    public UserBuilder networkCount(@Nullable Integer networkCount) {
        this.networkCount = networkCount;
        return this;
    }

    public UserBuilder connectionCount(@Nullable Integer connectionCount) {
        this.connectionCount = connectionCount;
        return this;
    }

    public User build() {
        User user = new User();
        user.id = id;
        user.email = email;
        user.username = username;
        user.networkCount = networkCount;
        user.connectionCount = connectionCount;
        return user;
    }

    public static UserBuilder fullUser() {
        return new UserBuilder()
                .email("test@example.com")
                .networkCount(0)
                .connectionCount(0);
    }
}
