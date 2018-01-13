package com.ryanfung.cnct.activities;

import android.view.View;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.builders.UserBuilder;
import com.ryanfung.cnct.contracts.MainContract;
import com.ryanfung.cnct.models.User;

import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Shadows.shadowOf;

public class MainActivityTests extends ActivityTests {

    @Mock
    private MainContract.Presenter presenter;
    private MainActivity activity;

    @Override
    public void setup() {
        super.setup();

        activity = spy(buildActivity(MainActivity.class).get());
        activity.setPresenter(presenter);
        activity.onCreate(null);
    }

    @Test
    public void onStartCallsLoadUser() {
        activity.onStart();
        verify(presenter).loadUser();
    }

    @Test
    public void onDestroyDetachesView() {
        activity.onDestroy();
        verify(presenter).viewDetached();
    }

    @Test
    public void onCreateOptionsMenu() {
        assertThat(activity.onCreateOptionsMenu(new RoboMenu())).isTrue();
    }

    @Test
    public void onOptionsItemSelectedSettings() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_settings));

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(SettingsActivity.class.getName());
    }

    @Test
    public void onOptionsItemSelectedSignOut() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_sign_out));

        verify(presenter).signOut();
    }

    @Test
    public void userLoadedSetsViews() {
        User user = UserBuilder.fullUser().build();
        activity.onUserLoaded(user);

        assertThat(activity.actionBarUserIconView.getInitials()).isEqualTo(user.getInitials());
        assertThat(activity.actionBarTitleView.getText().toString()).isEqualTo(user.username);
        assertThat(activity.actionBarSubtitleView.getText().toString()).isEqualTo(user.email);
    }

    @Test
    public void signedOutStartsWelcomeActivity() {
        activity.onSignedOut();

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(WelcomeActivity.class.getName());
    }

    @Test
    public void signedOutFinishesSelf() {
        activity.onSignedOut();

        assertThat(activity.isFinishing()).isTrue();
    }

    @Test
    public void editProfile() {
        activity.editProfile(null);

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(EditProfileActivity.class.getName());
    }

    @Test
    public void fabShowingHidesContainer() {
        // to show it
        activity.fab();

        // to dismiss it
        activity.fab();

        assertThat(activity.subItemsContainerView.getVisibility()).isEqualTo(View.GONE);
    }


    @Test
    public void fabHidingShowsContainer() {
        activity.fab();

        assertThat(activity.subItemsContainerView.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void addNetworksStartsActivity() {
        activity.fab();
        activity.addNetwork();

        assertThat(shadowOf(activity)
                .getNextStartedActivity()
                .getComponent()
                .getClassName()).isEqualTo(AddNetworkActivity.class.getName());
    }

    @Test
    public void addConnectionStartsActivity() {
        activity.addConnection();
    }

}
