package com.ryanfung.cnct.activities;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.contracts.SignInContract;

import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.fakes.RoboMenuItem;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Shadows.shadowOf;

public class SignInActivityTests extends ActivityTests {

    @Mock
    private SignInContract.Presenter presenter;
    private SignInActivity activity;

    @Override
    public void setup() {
        super.setup();

        activity = spy(buildActivity(SignInActivity.class).get());
        activity.setPresenter(presenter);
        activity.onCreate(null);
    }

    @Test
    public void onHomePressed() {
        activity.onOptionsItemSelected(new RoboMenuItem(android.R.id.home));
        verify(activity).onBackPressed();
    }

    @Test
    public void onSubmitDisablesEmail() {
        activity.onSubmit(null);
        assertThat(activity.emailEditText.isEnabled()).isFalse();
    }

    @Test
    public void onSubmitDisablesPassword() {
        activity.onSubmit(null);
        assertThat(activity.passwordEditText.isEnabled()).isFalse();
    }

    @Test
    public void onSubmitDisablesSubmit() {
        activity.onSubmit(null);
        assertThat(activity.submitButton.isEnabled()).isFalse();
    }

    @Test
    public void onSubmitChangesText() {
        activity.onSubmit(null);
        assertThat(activity.submitButton.getText().toString())
                .isEqualTo(activity.getString(R.string.sign_in_processing));
    }

    @Test
    public void onSubmitCallsAuthenticate() {
        activity.onSubmit(null);
        verify(presenter).authenticate(anyString(), anyString());
    }

    @Test
    public void authenticationSuccessStartsMainActivity() {
        activity.onAuthenticationSuccess();

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(MainActivity.class.getName());
    }

    @Test
    public void authenticationSuccessFinishesSelf() {
        activity.onAuthenticationSuccess();

        assertThat(activity.isFinishing()).isTrue();
    }

    @Test
    public void authenticationFailureEnablesEmail() {
        activity.onAuthenticationFailure();
        assertThat(activity.emailEditText.isEnabled()).isTrue();
    }

    @Test
    public void authenticationFailureEnablesPassword() {
        activity.onAuthenticationFailure();
        assertThat(activity.passwordEditText.isEnabled()).isTrue();
    }

    @Test
    public void authenticationFailureEnablesSubmit() {
        activity.onAuthenticationFailure();
        assertThat(activity.submitButton.isEnabled()).isTrue();
    }

}
