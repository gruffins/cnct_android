package com.ryanfung.cnct.activities;

import com.ryanfung.cnct.contracts.WelcomeContract;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Robolectric.getForegroundThreadScheduler;
import static org.robolectric.Shadows.shadowOf;

public class WelcomeActivityTests extends ActivityTests {

    @Mock
    private WelcomeContract.Presenter presenter;
    private WelcomeActivity activity;

    @Override
    public void setup() {
        super.setup();

        activity = buildActivity(WelcomeActivity.class).get();
        activity.setPresenter(presenter);
        activity.onCreate(null);
    }

    @Test
    public void onErrorEnablesUsernameEditText() {
        activity.onError(new HashMap<String, List<String>>());
        assertThat(activity.usernameEditText.isEnabled()).isTrue();
    }

    @Test
    public void onErrorEnablesEmailEditText() {
        activity.onError(new HashMap<String, List<String>>());
        assertThat(activity.emailEditText.isEnabled()).isTrue();
    }

    @Test
    public void onErrorEnablesPasswordEditText() {
        activity.onError(new HashMap<String, List<String>>());
        assertThat(activity.passwordEditText.isEnabled()).isTrue();
    }

    @Test
    public void onErrorEnablesSubmit() {
        activity.onError(new HashMap<String, List<String>>());
        assertThat(activity.submitView.isEnabled()).isTrue();
    }

    @Test
    public void onErrorWithUsername() {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("username", Collections.singletonList("is required"));

        activity.onError(errors);

        assertThat(activity.usernameLayout.getError()).isEqualTo("is required");
    }

    @Test
    public void onErrorWithEmail() {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("email", Collections.singletonList("is invalid"));

        activity.onError(errors);

        assertThat(activity.emailLayout.getError()).isEqualTo("is invalid");
    }

    @Test
    public void onErrorWithPassword() {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("password", Collections.singletonList("is required"));

        activity.onError(errors);

        assertThat(activity.passwordLayout.getError()).isEqualTo("is required");
    }

    @Test
    public void onSuccessStartsMainActivity() {
        activity.onSuccess();

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(MainActivity.class.getName());
    }

    @Test
    public void onSuccessFinishesSelf() {
        activity.onSuccess();

        assertThat(activity.isFinishing()).isTrue();
    }

    @Test
    public void onSignInStartsSignInActivity() {
        activity.onSignIn(null);

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(SignInActivity.class.getName());
    }


}
