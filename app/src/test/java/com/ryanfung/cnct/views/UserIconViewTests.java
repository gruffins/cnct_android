package com.ryanfung.cnct.views;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class UserIconViewTests {

    private UserIconView view;

    @Before
    public void setup() {
        view = new UserIconView(RuntimeEnvironment.application);
    }

    @Test
    public void getSetInitials() {
        view.setInitials("abc");
        assertThat(view.getInitials()).isEqualTo("abc");
    }

}
