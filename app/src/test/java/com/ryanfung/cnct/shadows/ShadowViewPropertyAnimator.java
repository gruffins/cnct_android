package com.ryanfung.cnct.shadows;

import android.animation.Animator;
import android.view.ViewPropertyAnimator;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

@Implements(ViewPropertyAnimator.class)
public class ShadowViewPropertyAnimator {

    @RealObject
    private ViewPropertyAnimator animator;
    private Animator.AnimatorListener listener;

    @Implementation
    public ViewPropertyAnimator setListener(Animator.AnimatorListener listener) {
        this.listener = listener;
        return animator;
    }

    @Implementation
    public void start() {
        if (listener != null) {
            listener.onAnimationStart(null);
            listener.onAnimationEnd(null);
        }
    }
}
