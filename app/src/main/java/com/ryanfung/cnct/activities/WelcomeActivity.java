package com.ryanfung.cnct.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.animation.Listener;
import com.ryanfung.cnct.contracts.WelcomeContract;
import com.ryanfung.cnct.presenters.WelcomePresenter;
import com.ryanfung.cnct.rx.BaseCompletableObserver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.ryanfung.cnct.utils.AndroidUtil.canTarget;

public class WelcomeActivity extends Activity implements WelcomeContract.View {

    @BindView(android.R.id.content)
    View contentView;

    @BindView(R.id.tagline)
    AppCompatTextView taglineView;

    @BindView(R.id.username_edit_text)
    TextInputEditText usernameEditText;

    @BindView(R.id.username_layout)
    TextInputLayout usernameLayout;

    @BindView(R.id.email_edit_text)
    TextInputEditText emailEditText;

    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;

    @BindView(R.id.password_edit_text)
    TextInputEditText passwordEditText;

    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @BindView(R.id.submit)
    AppCompatButton submitView;

    @BindView(R.id.sign_in)
    AppCompatTextView signInView;

    private WelcomeContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        setPresenter(new WelcomePresenter(getPreferences(), getApi(), this));

        addDisposable(Completable.complete()
                .delay(250, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .andThen(new FadeAnimation(taglineView))
                .andThen(new FadeAnimation(usernameLayout))
                .andThen(new FadeAnimation(emailLayout))
                .andThen(new FadeAnimation(passwordLayout))
                .andThen(new FadeAnimation(submitView))
                .andThen(new FadeAnimation(signInView))
                .subscribe());

        setupWindowAnimations();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.viewDetached();
    }

    public void setPresenter(WelcomeContract.Presenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void onError(Map<String, List<String>> errors) {
        usernameEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        submitView.setEnabled(true);

        if (errors.get("username") != null) {
            usernameLayout.setError(errors.get("username").get(0));
        }

        if (errors.get("email") != null) {
            emailLayout.setError(errors.get("email").get(0));
        }

        if (errors.get("password") != null) {
            passwordLayout.setError(errors.get("password").get(0));
        }

        submitView.setText(R.string.welcome_submit);
        Toast.makeText(this, R.string.welcome_could_not_create_account, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        final WelcomeActivity activity = this;

        addDisposable(new FadeAnimation(signInView, true)
                .andThen(new FadeAnimation(submitView, true))
                .andThen(new FadeAnimation(passwordLayout, true))
                .andThen(new FadeAnimation(emailLayout, true))
                .andThen(new FadeAnimation(usernameLayout, true))
                .andThen(new FadeAnimation(taglineView, true))
                .subscribeWith(new BaseCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle();

                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent, bundle);
                        finish();
                    }
                }));
    }

    // =============================================================================================
    // Buttons
    // =============================================================================================

    @OnClick(R.id.submit)
    public void onSubmit(View view) {
        usernameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        submitView.setEnabled(false);
        submitView.setText(R.string.welcome_creating_account);

        usernameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);

        presenter.createAccount(
                usernameEditText.getText().toString(),
                emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @OnClick(R.id.sign_in)
    public void onSignIn(View view) {
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
        startActivity(new Intent(this, SignInActivity.class), bundle);
    }

    // =============================================================================================
    // Internals
    // =============================================================================================

    private void setupWindowAnimations() {
        if (canTarget(21)) {
            Slide slide = new Slide(Gravity.START);
            slide.setDuration(500);
            slide.excludeTarget(android.R.id.statusBarBackground, true);
            slide.excludeTarget(android.R.id.navigationBarBackground, true);

            Fade fade = new Fade();
            fade.setDuration(500);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(fade);
        }
    }

    // =============================================================================================
    // Animations
    // =============================================================================================

    private static class FadeAnimation extends Completable {

        private View view;
        private boolean reverse;

        FadeAnimation(@NonNull View view) {
            this(view, false);
        }

        FadeAnimation(@NonNull View view, boolean reverse) {
            this.view = view;
            this.reverse = reverse;
        }

        @Override
        protected void subscribeActual(final CompletableObserver observer) {
            ObjectAnimator translation, alpha;

            if (reverse) {
                translation = ObjectAnimator.ofFloat(view, "translationY", 0, -50);
                alpha = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
            } else {
                translation = ObjectAnimator.ofFloat(view, "translationY", -50, 0);
                alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
            }

            AnimatorSet set = new AnimatorSet();
            set.playTogether(translation, alpha);
            set.addListener(new Listener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    observer.onComplete();
                }
            });
            set.setDuration(50);
            set.start();
        }
    }

}
