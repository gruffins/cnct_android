package com.ryanfung.cnct.models;

import com.ryanfung.cnct.builders.UserBuilder;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UserTests {

    @Test
    public void builderFullUser() {
        User user = UserBuilder.fullUser().build();

        assertThat(user.email).isNotNull();
        assertThat(user.networkCount).isNotNull();
        assertThat(user.connectionCount).isNotNull();
    }

    @Test
    public void userWithSameIdsAreEqual() {
        User user1 = new UserBuilder().build();
        User user2 = new UserBuilder().id(user1.id).build();

        assertThat(user1).isEqualTo(user2);
    }
}
