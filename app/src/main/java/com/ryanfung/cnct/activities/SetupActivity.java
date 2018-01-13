package com.ryanfung.cnct.activities;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.AppCompatButton;
import android.transition.Fade;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.ryanfung.cnct.R;
import com.ryanfung.cnct.animation.Listener;
import com.ryanfung.cnct.contracts.SetupContract;
import com.ryanfung.cnct.exceptions.PermissionException;
import com.ryanfung.cnct.exceptions.RSAKeyPairGeneratorException;
import com.ryanfung.cnct.presenters.SetupPresenter;
import com.ryanfung.cnct.rx.BaseCompletableObserver;
import com.ryanfung.cnct.rx.RSAKeyPairGenerator;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.ryanfung.cnct.utils.AndroidUtil.canTarget;
import static com.ryanfung.cnct.utils.AndroidUtil.getMetaData;
import static com.ryanfung.cnct.utils.AndroidUtil.hasPermission;

public class SetupActivity extends Activity implements SetupContract.View {

    private static final String CLIENT_AUTHORIZATION = "com.ryanfung.cnct.client_authorization";

    public static final int REQUEST_CODE_COARSE_PERMISSION = 100;

    @BindView(R.id.loading_view)
    LottieAnimationView loadingView;

    @BindView(R.id.success_view)
    LottieAnimationView successView;

    @BindView(R.id.failure_view)
    LottieAnimationView failureView;

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.retry_button)
    AppCompatButton retryButton;

    SetupContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        ButterKnife.bind(this);

        setPresenter(new SetupPresenter(getPreferences(), getApi(), this));

        setup();
        setupWindowAnimations();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.viewDetached();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_COARSE_PERMISSION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setup();
        } else {
            onFailed(new PermissionException());
        }
    }

    public void setPresenter(SetupContract.Presenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    // =============================================================================================
    // Buttons
    // =============================================================================================

    @OnClick(R.id.retry_button)
    public void retry() {
        successView.setVisibility(View.INVISIBLE);
        failureView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.VISIBLE);
        loadingView.playAnimation();

        addDisposable(new FadeAnimation(textView, 0, 150)
                .mergeWith(new FadeAnimation(retryButton, 0, 150))
                .subscribeWith(new BaseCompletableObserver() {
                    @Override
                    public void onComplete() {
                        setup();
                    }
                }));
    }


    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void onSuccess() {
        final SetupActivity activity = this;

        textView.setText(R.string.setup_success);

        addDisposable(Completable.complete()
                .delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .andThen(new LottieAnimation(loadingView, successView))
                .mergeWith(new FadeAnimation(textView, 1, 500))
                .delay(1500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeWith(new BaseCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();
                        startActivity(new Intent(activity, WelcomeActivity.class), bundle);
                        activity.finish();
                    }
                }));
    }

    @Override
    public void onFailed(Throwable tr) {
        if (tr instanceof RSAKeyPairGeneratorException) {
            textView.setText(R.string.setup_error);
        } else if (tr instanceof PermissionException) {
            textView.setText(R.string.setup_permission);
        } else {
            textView.setText(R.string.setup_network);
        }

        addDisposable(new LottieAnimation(loadingView, failureView)
                .subscribeOn(AndroidSchedulers.mainThread())
                .mergeWith(new FadeAnimation(textView, 1, 500))
                .mergeWith(new FadeAnimation(retryButton, 1, 500))
                .subscribe());
    }

    @Override
    public void onRequestPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION },
                REQUEST_CODE_COARSE_PERMISSION);
    }

    // =============================================================================================
    // Internals
    // =============================================================================================

    private void setup() {
        presenter.setup(
                getMetaData(this, CLIENT_AUTHORIZATION, ""),
                new RSAKeyPairGenerator(),
                hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private void setupWindowAnimations() {
        if (canTarget(21)) {
            Fade fade = new Fade();
            fade.setDuration(500);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setExitTransition(fade);
        }
    }

    // =============================================================================================
    // Animations
    // =============================================================================================

    private static class LottieAnimation extends Completable {

        private LottieAnimationView loadingView;
        private LottieAnimationView newView;

        LottieAnimation(@NonNull LottieAnimationView loadingView, @NonNull LottieAnimationView newView) {
            this.newView = newView;
            this.loadingView = loadingView;
        }

        @Override
        protected void subscribeActual(final CompletableObserver observer) {
            loadingView.animate()
                    .alpha(0)
                    .setDuration(150)
                    .setListener(new Listener() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            loadingView.cancelAnimation();
                            loadingView.setVisibility(View.INVISIBLE);
                        }
                    })
                    .start();

            newView.animate()
                    .alpha(1)
                    .setDuration(150)
                    .setListener(new Listener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            newView.setVisibility(View.VISIBLE);
                            newView.playAnimation();
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            observer.onComplete();
                        }
                    })
                    .start();
        }
    }

    private static class FadeAnimation extends Completable {

        private float toAlpha;
        private View view;
        private long duration;

        FadeAnimation(View view, float toAlpha, long duration) {
            this.view = view;
            this.toAlpha = toAlpha;
            this.duration = duration;
        }

        @Override
        protected void subscribeActual(final CompletableObserver observer) {
            ViewPropertyAnimator animator = view.animate();
            animator.setListener(new Listener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    observer.onComplete();
                }
            });
            animator.alpha(toAlpha);
            animator.setDuration(duration);
            animator.setStartDelay(0);
            animator.start();
        }
    }

}
