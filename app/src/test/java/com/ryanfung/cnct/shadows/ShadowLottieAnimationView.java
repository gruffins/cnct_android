package com.ryanfung.cnct.shadows;

import android.animation.Animator;

import com.airbnb.lottie.LottieAnimationView;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowView;

import java.util.LinkedList;
import java.util.List;

@Implements(LottieAnimationView.class)
public class ShadowLottieAnimationView extends ShadowView {

    @RealObject
    private LottieAnimationView view;
    private List<Animator.AnimatorListener> listeners = new LinkedList<>();


    @Implementation
    public void addAnimatorListener(Animator.AnimatorListener listener) {
        listeners.add(listener);
    }

    @Implementation
    public void playAnimation() {
        for (Animator.AnimatorListener listener : listeners) {
            listener.onAnimationStart(null);
            listener.onAnimationEnd(null);
        }
    }
}
