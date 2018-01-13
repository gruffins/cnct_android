package com.ryanfung.cnct.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ApplicationTests {

    private Application application;

    @Before
    public void setup() {
        application = (Application) RuntimeEnvironment.application;
    }

    @Test
    public void getPreferences() {
        assertThat(application.getPreferences()).isNotNull();
    }

    @Test
    public void getApi() {
        assertThat(application.getApi()).isNotNull();
    }
}
