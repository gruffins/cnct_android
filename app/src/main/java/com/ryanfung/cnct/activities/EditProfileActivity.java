package com.ryanfung.cnct.activities;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.ryanfung.cnct.R;
import com.ryanfung.cnct.animation.Listener;
import com.ryanfung.cnct.contracts.EditProfileContract;
import com.ryanfung.cnct.models.User;
import com.ryanfung.cnct.presenters.EditProfilePresenter;
import com.ryanfung.cnct.rx.BaseObserver;
import com.ryanfung.cnct.views.UserIconView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends Activity implements EditProfileContract.View {

    @BindView(R.id.edit_profile_user_icon_view)
    UserIconView userIconView;

    @BindView(R.id.edit_profile_username_input_layout)
    TextInputLayout usernameInputLayout;

    @BindView(R.id.edit_profile_username_edit_text)
    TextInputEditText usernameEditText;

    @BindView(R.id.edit_profile_email_input_layout)
    TextInputLayout emailInputLayout;

    @BindView(R.id.edit_profile_email_edit_text)
    TextInputEditText emailEditText;

    @BindView(R.id.edit_profile_current_password_input_layout)
    TextInputLayout currentPasswordInputLayout;

    @BindView(R.id.edit_profile_current_password_edit_text)
    TextInputEditText currentPasswordEditText;

    @BindView(R.id.edit_profile_password_input_layout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.edit_profile_password_edit_text)
    TextInputEditText passwordEditText;

    @BindView(R.id.edit_profile_loading_view)
    LottieAnimationView loadingView;

    @BindView(R.id.edit_profile_success_view)
    LottieAnimationView successView;

    private EditProfileContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setPresenter(new EditProfilePresenter(getPreferences(), getApi(), this));

        ButterKnife.bind(this);

        setupActionBar();
        setupTextListener();

        presenter.loadUser();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.viewDetached();
    }

    public void setPresenter(EditProfileContract.Presenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }
    }

    // =============================================================================================
    // Contract
    // =============================================================================================

    @Override
    public void onUserLoaded(User user) {
        userIconView.setInitials(user.getInitials());
        usernameEditText.setText(user.username);
        emailEditText.setText(user.email);
    }

    @Override
    public void onSaved() {
        userIconView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        successView.setVisibility(View.VISIBLE);

        loadingView.pauseAnimation();

        successView.addAnimatorListener(new Listener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                userIconView.setVisibility(View.VISIBLE);
                userIconView.setAlpha(0);
                userIconView.animate().alpha(1).setDuration(100).start();
                successView.animate().alpha(0).setDuration(100).start();

                ActivityCompat.finishAfterTransition(EditProfileActivity.this);
            }
        });
        successView.playAnimation();
    }

    @Override
    public void onSaveFailed(Map<String, List<String>> errors) {
        userIconView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        successView.setVisibility(View.GONE);

        loadingView.pauseAnimation();

        if (errors.containsKey("username")) {
            usernameInputLayout.setError(errors.get("username").get(0));
        }

        if (errors.containsKey("email")) {
            emailInputLayout.setError(errors.get("email").get(0));
        }

        if (errors.containsKey("current_password")) {
            currentPasswordInputLayout.setError(errors.get("current_password").get(0));
        }

        usernameEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        currentPasswordEditText.setEnabled(true);
        passwordEditText.setEnabled(true);

        Toast.makeText(this, R.string.edit_profile_failure, Toast.LENGTH_SHORT).show();
    }

    // =============================================================================================
    // Internals
    // =============================================================================================

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupTextListener() {
        addDisposable(RxTextView
                .textChanges(usernameEditText)
                .subscribeWith(new BaseObserver<CharSequence>() {
                    @Override
                    public void onNext(CharSequence string) {
                        String initials = string
                                .subSequence(0, Math.min(3, string.length()))
                                .toString()
                                .toUpperCase();

                        userIconView.setInitials(initials);
                    }
                }));
    }

    private void save() {
        userIconView.setVisibility(View.GONE);
        successView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        loadingView.playAnimation();

        usernameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        currentPasswordEditText.setEnabled(false);
        passwordEditText.setEnabled(false);

        usernameInputLayout.setError(null);
        emailInputLayout.setError(null);
        currentPasswordInputLayout.setError(null);

        presenter.save(
                usernameEditText.getText().toString(),
                emailEditText.getText().toString(),
                currentPasswordEditText.getText().toString(),
                passwordEditText.getText().toString());
    }
}
