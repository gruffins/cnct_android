package com.ryanfung.cnct.network;

import com.ryanfung.cnct.content.Preferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ApiBuilderTests {

    @Mock
    private Preferences preferences;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void build() {
        assertThat(new ApiBuilder(preferences, "http://example.com").build()).isNotNull();
    }
}
