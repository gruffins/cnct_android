package com.ryanfung.cnct.animation;

import android.animation.Animator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

public class ListenerTests {

    @Mock
    private Animator animator;
    private Listener listener;

    @Before
    public void setup() {
        initMocks(this);
        listener = new Listener();
    }

    @Test
    public void onAnimatorStart() {
        listener.onAnimationStart(animator);
    }

    @Test
    public void onAnimationEnd() {
        listener.onAnimationEnd(animator);
    }

    @Test
    public void onAnimationCancel() {
        listener.onAnimationCancel(animator);
    }

    @Test
    public void onAnimationRepeat() {
        listener.onAnimationRepeat(animator);
    }
}
