package com.ryanfung.cnct.exceptions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApiExceptionTests {

    @Mock
    private HttpException httpException;
    private ApiException exception;

    @Before
    public void setup() throws Exception {
        initMocks(this);

        Response response = Response.error(400,
                ResponseBody.create(
                        MediaType.parse("application/json"),
                        "{\"errors\":{\"key\":[\"value\"]}}"));

        doReturn(response).when(httpException).response();

        exception = new ApiException(httpException);
    }

    @Test
    public void parsesHttpException() {
        Map<String, List<String>> errors = exception.getErrors();
        assertThat(errors.get("key").get(0)).isEqualTo("value");
    }
}
