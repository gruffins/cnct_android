package com.ryanfung.cnct.exceptions;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ryanfung.cnct.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ApiException extends Exception {

    private static final String TAG = "ApiException";
    private static final String ERRORS = "errors";

    private Map<String, List<String>> errors;

    public ApiException(@NonNull HttpException exception) {
        errors = new HashMap<>();

        try {
            ResponseBody response = exception.response().errorBody();
            Gson gson = GsonUtil.defaultGson();

            if (response != null) {
                Type type = new TypeToken<Map<String, Map<String, List<String>>>>(){}.getType();
                Map<String, Map<String, List<String>>> map = gson.fromJson(response.charStream(), type);
                errors.putAll(map.get(ERRORS));
            }
        } catch (Exception ex) {
            Log.e(TAG, "Could not parse http exception.", ex);
        }
    }

    public Map<String, List<String>> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

}
