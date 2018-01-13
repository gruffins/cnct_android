package com.ryanfung.cnct.network;

import android.support.annotation.NonNull;

import com.ryanfung.cnct.builders.AccessTokenBuilder;
import com.ryanfung.cnct.content.Preferences;
import com.ryanfung.cnct.models.AccessToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class InterceptorTests {

    @Mock
    private Preferences preferences;
    private Interceptor interceptor;
    private AccessToken token;

    @Before
    public void setup() {
        initMocks(this);

        interceptor = new Interceptor(preferences);
        token = new AccessTokenBuilder().build();
    }

    @Test
    public void addAuthorizationHeader() throws Exception {
        doReturn(token).when(preferences).getAccessToken();

        TestChain chain = new TestChain();
        Response response = interceptor.intercept(chain);

        assertThat(response.request().header("Authorization")).isEqualTo(String.format(Locale.US, "Bearer %s", token.accessToken));
    }

    @Test
    public void doesntOverwriteAuthorizationHeader() throws Exception {
        doReturn(new AccessTokenBuilder().build()).when(preferences).getAccessToken();

        Request request = new Request.Builder()
                .url("http://example.com")
                .addHeader("Authorization", "Bearer test")
                .build();

        TestChain chain = new TestChain(request);
        Response response = interceptor.intercept(chain);

        assertThat(response.request().header("Authorization")).isNotEqualTo(String.format(Locale.US, "Bearer %s", token.accessToken));
    }

    // =============================================================================================
    // Chain
    // =============================================================================================

    private static class TestChain implements okhttp3.Interceptor.Chain {

        @NonNull
        private Request request;

        public TestChain() {
            this(null);
        }

        public TestChain(@Nullable Request request) {
            this.request = request == null ? new Request.Builder().url("http://example.com").build() : request;
        }

        @Override
        public Request request() {
            return request;
        }

        @Override
        public Response proceed(Request request) throws IOException {
            return new Response.Builder()
                    .protocol(Protocol.HTTP_2)
                    .request(request)
                    .code(200)
                    .message("message")
                    .build();
        }

        @Nullable
        @Override
        public Connection connection() {
            return null;
        }

        @Override
        public Call call() {
            return null;
        }

        @Override
        public int connectTimeoutMillis() {
            return 0;
        }

        @Override
        public okhttp3.Interceptor.Chain withConnectTimeout(int timeout, TimeUnit unit) {
            return null;
        }

        @Override
        public int readTimeoutMillis() {
            return 0;
        }

        @Override
        public okhttp3.Interceptor.Chain withReadTimeout(int timeout, TimeUnit unit) {
            return null;
        }

        @Override
        public int writeTimeoutMillis() {
            return 0;
        }

        @Override
        public okhttp3.Interceptor.Chain withWriteTimeout(int timeout, TimeUnit unit) {
            return null;
        }

    }

}
