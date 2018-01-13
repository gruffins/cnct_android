package com.ryanfung.cnct.activities;

import com.ryanfung.cnct.contracts.LaunchContract;

import org.junit.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Shadows.shadowOf;

public class LaunchActivityTests extends ActivityTests {

    @Mock
    private LaunchContract.Presenter presenter;
    private LaunchActivity activity;

    @Override
    public void setup() {
        super.setup();

        activity = buildActivity(LaunchActivity.class).get();
        activity.setPresenter(presenter);
    }

    @Test
    public void onCreateChecksState() {
        activity.onCreate(null);
        verify(presenter).checkState(anyBoolean());
    }

    @Test
    public void setupStartsActivity() {
        activity.onSetup();

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(SetupActivity.class.getName());
    }

    @Test
    public void setupFinishesSelf() {
        activity.onSetup();

        assertThat(activity.isFinishing()).isTrue();
    }

    @Test
    public void authedStartsActivity() {
        activity.onAuthed();

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(MainActivity.class.getName());
    }

    @Test
    public void authedFinishesSelf() {
        activity.onAuthed();

        assertThat(activity.isFinishing()).isTrue();
    }

    @Test
    public void welcomeStartsActivity() {
        activity.onWelcome();

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(WelcomeActivity.class.getName());
    }

    @Test
    public void welcomeFinishesSelf() {
        activity.onWelcome();

        assertThat(activity.isFinishing()).isTrue();
    }
}
