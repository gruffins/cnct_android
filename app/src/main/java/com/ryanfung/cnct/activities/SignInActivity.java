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
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.animation.Listener;
import com.ryanfung.cnct.contracts.SignInContract;
import com.ryanfung.cnct.presenters.SignInPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.ryanfung.cnct.utils.AndroidUtil.canTarget;

public class SignInActivity extends Activity implements SignInContract.View {

    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;

    @BindView(R.id.email_edit_text)
    TextInputEditText emailEditText;

    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @BindView(R.id.password_edit_text)
    TextInputEditText passwordEditText;

    @BindView(R.id.submit)
    AppCompatButton submitButton;

    private SignInContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        setPresenter(new SignInPresenter(getPreferences(), getApi(), this));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupWindowAnimations();

        addDisposable(Completable.complete()
                .delay(250, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .andThen(new FadeAnimation(emailLayout))
                .andThen(new FadeAnimation(passwordLayout))
                .andThen(new FadeAnimation(submitButton))
                .subscribe());
    }

    public void setPresenter(SignInContract.Presenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.viewDetached();
    }

    // =============================================================================================
    // Buttons
    // =============================================================================================

    @OnClick(R.id.submit)
    public void onSubmit(View view) {
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        submitButton.setEnabled(false);

        submitButton.setText(R.string.sign_in_processing);

        presenter.authenticate(
                emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void onAuthenticationSuccess() {
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, bundle);
        finish();
    }

    @Override
    public void onAuthenticationFailure() {
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        submitButton.setEnabled(true);

        submitButton.setText(R.string.sign_in_submit);

        Toast.makeText(this, R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
    }

    // =============================================================================================
    // Animations
    // =============================================================================================

    private static class FadeAnimation extends Completable {

        private View view;

        FadeAnimation(@NonNull View view) {
            this.view = view;
        }

        @Override
        protected void subscribeActual(final CompletableObserver observer) {
            ObjectAnimator translation, alpha;

            translation = ObjectAnimator.ofFloat(view, "translationY", -50, 0);
            alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(translation, alpha);
            set.addListener(new Listener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    observer.onComplete();
                }
            });
            set.setDuration(100);
            set.start();
        }

    }

    // =============================================================================================
    // Internals
    // =============================================================================================

    private void setupWindowAnimations() {
        if (canTarget(21)) {
            Fade fade = new Fade();
            fade.setDuration(500);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }
    }
}
