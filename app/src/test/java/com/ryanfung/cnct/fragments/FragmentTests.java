package com.ryanfung.cnct.fragments;

import com.ryanfung.cnct.helpers.ImmediateSchedulersRule;
import com.ryanfung.cnct.shadows.ShadowAnimatorSet;
import com.ryanfung.cnct.shadows.ShadowLottieAnimationView;
import com.ryanfung.cnct.shadows.ShadowViewPropertyAnimator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowAnimatorSet.class, ShadowViewPropertyAnimator.class, ShadowLottieAnimationView.class})
public abstract class FragmentTests {

    @Rule
    public TestRule rx = new ImmediateSchedulersRule();

    @Before
    public void setup() {
        initMocks(this);
    }
}
