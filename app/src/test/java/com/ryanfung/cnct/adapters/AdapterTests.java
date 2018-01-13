package com.ryanfung.cnct.adapters;

import android.content.Context;

import com.ryanfung.cnct.helpers.ImmediateSchedulersRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public abstract class AdapterTests {

    @Rule
    public TestRule rx = new ImmediateSchedulersRule();

    @Before
    public void setup() {
        initMocks(this);
    }

    protected Context getContext() {
        return RuntimeEnvironment.application;
    }
}
