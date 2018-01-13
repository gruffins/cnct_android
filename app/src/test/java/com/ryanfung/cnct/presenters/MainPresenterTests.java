package com.ryanfung.cnct.presenters;

import com.ryanfung.cnct.builders.UserBuilder;
import com.ryanfung.cnct.contracts.MainContract;
import com.ryanfung.cnct.models.User;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTests extends PresenterTests {

    @Mock
    private MainContract.View view;
    private MainPresenter presenter;
    private User user;

    @Override
    public void setup() {
        super.setup();

        user = UserBuilder.fullUser().build();
        presenter = new MainPresenter(preferences, api, view);

        when(preferences.getCurrentUser()).thenReturn(user);
    }

    @After
    public void teardown() {
        presenter.viewDetached();
    }

    @Test
    public void loadUserCallsView() {
        presenter.loadUser();
        verify(view).onUserLoaded(eq(user));
    }

    @Test
    public void signOutClearsUser() {
        presenter.signOut();

        verify(preferences).clearUser();
    }

    @Test
    public void signOutCallsView() {
        presenter.signOut();

        verify(view).onSignedOut();
    }
}
