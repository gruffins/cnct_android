package com.ryanfung.cnct.presenters;

import com.ryanfung.cnct.builders.UserBuilder;
import com.ryanfung.cnct.contracts.EditProfileContract;
import com.ryanfung.cnct.models.User;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditProfilePresenterTests extends PresenterTests {

    @Mock
    private EditProfileContract.View view;
    private EditProfilePresenter presenter;
    private User user;

    @Override
    public void setup() {
        super.setup();

        user = UserBuilder.fullUser().build();
        presenter = new EditProfilePresenter(preferences, api, view);
        when(preferences.getCurrentUser()).thenReturn(user);
    }

    @After
    public void teardown() {
        presenter.viewDetached();
    }

    @Test
    public void loadUserCallsView() {
        presenter.loadUser();
        verify(view).onUserLoaded(any(User.class));
    }

    @Test
    public void saveWithNoChangesCallsSaved() {
        presenter.save(user.username, user.email, null, null);
        verify(view).onSaved();
    }

    @Test
    public void saveWithChangesCallsSaved() {
        when(api.updateUser(anyString(), anyMap())).thenReturn(Observable.just(user));
        presenter.save(user.username + "b", null, "password", "password");
        verify(view).onSaved();
    }

    @Test
    public void saveSuccessSetsCurrentUser() {
        when(api.updateUser(anyString(), anyMap())).thenReturn(Observable.just(user));
        presenter.save(user.username + "b", null, null, null);
        verify(preferences).setCurrentUser(any(User.class));
    }

    @Test
    public void saveHttpExceptionCallsFailed() {
        Response response = Response.error(400,
                ResponseBody.create(MediaType.parse("application/json"), "{}"));

        when(api.updateUser(anyString(), anyMap()))
                .thenReturn(Observable.<User>error(new HttpException(response)));

        presenter.save(user.username + "b", null, null, null);
        verify(view).onSaveFailed(anyMap());
    }

    @Test
    public void saveExceptionCallsFailed() {
        when(api.updateUser(anyString(), anyMap()))
                .thenReturn(Observable.<User>error(new Exception("test")));
        presenter.save(user.username + "b", null, null, null);
        verify(view).onSaveFailed(anyMap());
    }
}
