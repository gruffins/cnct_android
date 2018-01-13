package com.ryanfung.cnct.dialogs;

import android.app.Activity;
import android.content.Context;

import com.ryanfung.cnct.helpers.ImmediateSchedulersRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(RobolectricTestRunner.class)
public abstract class DialogTests {

    @Rule
    public TestRule rx = new ImmediateSchedulersRule();

    private Activity activity;

    @Before
    public void setup() {
        initMocks(this);

        activity = buildActivity(Activity.class).get();
    }

    protected Context getContext() {
        return activity;
    }

}
