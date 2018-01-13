package com.ryanfung.cnct.content;

import android.content.Context;
import android.content.SharedPreferences;

import com.ryanfung.cnct.builders.AccessTokenBuilder;
import com.ryanfung.cnct.builders.DeviceBuilder;
import com.ryanfung.cnct.builders.NetworkBuilder;
import com.ryanfung.cnct.builders.UserBuilder;
import com.ryanfung.cnct.models.AccessToken;
import com.ryanfung.cnct.models.Device;
import com.ryanfung.cnct.models.Network;
import com.ryanfung.cnct.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class PreferencesTests {

    private static final String SHARED_PREFERENCES = "test";

    private Preferences preferences;
    private SharedPreferences sharedPreferences;

    @Before
    public void setup() {
        sharedPreferences = RuntimeEnvironment.application.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        preferences = new Preferences(sharedPreferences);
    }

    @After
    public void teardown() {
        preferences.clear();
    }

    @Test
    public void getSetClientAccessToken() {
        AccessToken token = new AccessTokenBuilder().build();
        preferences.setClientAccessToken(token);
        assertThat(preferences.getClientAccessToken()).isEqualTo(token);
    }

    @Test
    public void clearClientAccessToken() {
        preferences.setClientAccessToken(null);
        assertThat(preferences.getClientAccessToken()).isNull();
    }

    @Test
    public void getSetAccessToken() {
        AccessToken token = new AccessTokenBuilder().build();
        preferences.setAccessToken(token);
        assertThat(preferences.getAccessToken()).isEqualTo(token);
    }

    @Test
    public void clearAccessToken() {
        preferences.setAccessToken(null);
        assertThat(preferences.getAccessToken()).isNull();
    }

    @Test
    public void getSetPublicKey() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);

        PublicKey key = generator.genKeyPair().getPublic();

        preferences.setPublicKey(key);
        assertThat(preferences.getPublicKey()).isEqualTo(key);
    }

    @Test
    public void clearPublicKey() {
        preferences.setPublicKey(null);
        assertThat(preferences.getPublicKey()).isNull();
    }

    @Test
    public void getSetPrivateKey() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);

        PrivateKey key = generator.genKeyPair().getPrivate();

        preferences.setPrivateKey(key);
        assertThat(preferences.getPrivateKey()).isEqualTo(key);
    }

    @Test
    public void clearPrivateKey() {
        preferences.setPrivateKey(null);
        assertThat(preferences.getPrivateKey()).isNull();
    }

    @Test
    public void getSetCurrentUser() {
        User user = new UserBuilder().build();
        preferences.setCurrentUser(user);
        assertThat(preferences.getCurrentUser()).isEqualTo(user);
    }

    @Test
    public void clearCurrentUser() {
        preferences.setCurrentUser(null);
        assertThat(preferences.getCurrentUser()).isNull();
    }

    @Test
    public void clearUserClearsUser() {
        preferences.clearUser();
        assertThat(preferences.getCurrentUser()).isNull();
    }

    @Test
    public void clearUserClearsAccessToken() {
        preferences.clearUser();
        assertThat(preferences.getAccessToken()).isNull();
    }

    @Test
    public void getSetLocalNetworks() {
        Network network = NetworkBuilder.local().build();
        List<Network> networks = Collections.singletonList(network);
        preferences.setLocalNetworks(networks);

        Network parsed = preferences.getLocalNetworks().get(0);

        assertThat(parsed).isEqualTo(network);
    }

    @Test
    public void getSetDevice() {
        Device device = new DeviceBuilder().build();
        preferences.setDevice(device);
        assertThat(preferences.getDevice()).isEqualTo(device);
    }
}
