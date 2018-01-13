package com.ryanfung.cnct.presenters;

import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.network.CnctApi;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public abstract class PresenterTests {

    @Mock
    protected Preferences preferences;

    @Mock
    protected CnctApi api;

    @Before
    public void setup() {
        initMocks(this);
    }
}
