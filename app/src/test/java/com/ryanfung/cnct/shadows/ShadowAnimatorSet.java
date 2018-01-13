package com.ryanfung.cnct.shadows;

import android.animation.Animator;
import android.animation.AnimatorSet;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

@Implements(AnimatorSet.class)
public class ShadowAnimatorSet {

    @RealObject
    private AnimatorSet animator;

    @Implementation
    public void start() {
        for (Animator.AnimatorListener listener : animator.getListeners()) {
            listener.onAnimationStart(animator);
            listener.onAnimationEnd(animator);
        }
    }
}
