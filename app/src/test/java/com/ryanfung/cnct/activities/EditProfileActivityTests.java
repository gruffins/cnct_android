package com.ryanfung.cnct.activities;

import android.view.View;

import com.ryanfung.cnct.R;
import com.ryanfung.cnct.builders.UserBuilder;
import com.ryanfung.cnct.contracts.EditProfileContract;
import com.ryanfung.cnct.models.User;

import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;

public class EditProfileActivityTests extends ActivityTests {

    @Mock
    private EditProfileContract.Presenter presenter;
    private EditProfileActivity activity;

    @Override
    public void setup() {
        super.setup();

        activity = spy(buildActivity(EditProfileActivity.class).get());
        activity.setPresenter(presenter);
        activity.onCreate(null);
    }

    @Test
    public void onCreateCallsLoadUser() {
        verify(presenter).loadUser();
    }

    @Test
    public void onOptionsItemSelectedHome() {
        activity.onOptionsItemSelected(new RoboMenuItem(android.R.id.home));
        verify(activity).onBackPressed();
    }

    @Test
    public void onOptionsItemSelectedSaveSetsCorrectViewVisiblity() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_save));

        assertThat(activity.userIconView.getVisibility()).isEqualTo(View.GONE);
        assertThat(activity.successView.getVisibility()).isEqualTo(View.GONE);
        assertThat(activity.loadingView.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void onOptionsItemSelectedSaveDisablesTextFields() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_save));

        assertThat(activity.usernameEditText.isEnabled()).isFalse();
        assertThat(activity.emailEditText.isEnabled()).isFalse();
        assertThat(activity.currentPasswordEditText.isEnabled()).isFalse();
        assertThat(activity.passwordEditText.isEnabled()).isFalse();
    }

    @Test
    public void onOptionsItemSelectedSaveClearsErrors() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_save));

        assertThat(activity.usernameInputLayout.getError()).isNull();
        assertThat(activity.emailInputLayout.getError()).isNull();
        assertThat(activity.currentPasswordInputLayout.getError()).isNull();
    }

    @Test
    public void onOptionsItemSelectedSaveCallsPresenter() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_save));

        verify(presenter).save(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void onCreateOptionsMenu() {
        assertThat(activity.onCreateOptionsMenu(new RoboMenu())).isTrue();
    }

    @Test
    public void onDestroyDetachesView() {
        activity.onDestroy();
        verify(presenter).viewDetached();
    }

    @Test
    public void userLoadedSetsUserIcon() {
        User user = UserBuilder.fullUser().build();
        activity.onUserLoaded(user);
        assertThat(activity.userIconView.getInitials()).isEqualTo(user.getInitials());
    }

    @Test
    public void userLoadedSetsUsername() {
        User user = UserBuilder.fullUser().build();
        activity.onUserLoaded(user);
        assertThat(activity.usernameEditText.getText().toString()).isEqualTo(user.username);
    }

    @Test
    public void userLoadedSetsEmail() {
        User user = UserBuilder.fullUser().build();
        activity.onUserLoaded(user);
        assertThat(activity.emailEditText.getText().toString()).isEqualTo(user.email);
    }

    @Test
    public void savedSetsViewVisiblity() {
        activity.onSaved();

        assertThat(activity.loadingView.getVisibility()).isEqualTo(View.GONE);
        assertThat(activity.successView.getVisibility()).isEqualTo(View.VISIBLE);
    }

    @Test
    public void savedFinishesActivity() {
        activity.onSaved();

        verify(activity).finishAfterTransition();
    }

    @Test
    public void saveFailedSetsViewVisibility() {
        activity.onSaveFailed(new HashMap<String, List<String>>());

        assertThat(activity.userIconView.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(activity.loadingView.getVisibility()).isEqualTo(View.GONE);
        assertThat(activity.successView.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void saveFailedEnablesTextFields() {
        activity.onSaveFailed(new HashMap<String, List<String>>());

        assertThat(activity.usernameEditText.isEnabled()).isTrue();
        assertThat(activity.emailEditText.isEnabled()).isTrue();
        assertThat(activity.currentPasswordEditText.isEnabled()).isTrue();
        assertThat(activity.passwordEditText.isEnabled()).isTrue();
    }

    @Test
    public void saveFailedWithUsernameError() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("username", Collections.singletonList("error"));

        activity.onSaveFailed(map);

        assertThat(activity.usernameInputLayout.getError()).isEqualTo("error");
    }

    @Test
    public void saveFailedWithEmailError() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("email", Collections.singletonList("error"));

        activity.onSaveFailed(map);

        assertThat(activity.emailInputLayout.getError()).isEqualTo("error");
    }

    @Test
    public void saveFailedWithPasswordError() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("current_password", Collections.singletonList("error"));

        activity.onSaveFailed(map);

        assertThat(activity.currentPasswordInputLayout.getError()).isEqualTo("error");
    }

}
