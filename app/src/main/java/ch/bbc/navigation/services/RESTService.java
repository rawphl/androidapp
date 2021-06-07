package ch.bbc.navigation.services;

import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

public class RESTService<T> extends HTTPService {
    public interface FailureCallback {
        public void onFailure(@NotNull Call call, @NotNull IOException e);
    }

    public interface SuccessCallback<T> {
        public void onSuccess(T data) throws IOException;
    }

    public interface ArraySuccessCallback<T> {
        public void onSuccess(List<T> data) throws IOException;
    }

    private JsonAdapter<T> jsonAdapter;
    private JsonAdapter<T> jsonListAdapter;
    private Class<T> clazz;

    public RESTService(Class<T> clazz) {
        Moshi moshi = new Moshi.Builder().build();
        jsonAdapter = moshi.adapter(clazz);
        Type type = Types.newParameterizedType(List.class, clazz);
        jsonListAdapter = moshi.adapter(type);
        this.clazz = clazz;
    }

    public void getJSONArray(String url, ArraySuccessCallback<T> success, FailureCallback failure) {
        get(url, (call, res) -> {
                    String body = res.body().string();
                    List<T> data = (List<T>) jsonListAdapter.fromJson(body);
                    success.onSuccess((List<T>) data);
                },
                (call, e) -> {
                    failure.onFailure(call, e);
                });
    }

    public void getJSON(String url, SuccessCallback<T> success, FailureCallback failure) {
        get(url, (call, res) -> {
                    String body = res.body().string();
                    T data = (T) jsonAdapter.fromJson(body);
                    success.onSuccess((T) data);
                },
                (call, e) -> {
                    failure.onFailure(call, e);
                });
    }

    public void postJSON(String url, T data, SuccessCallback<T> success, FailureCallback failure) {
        String postBody = jsonAdapter.toJson((T) data);

        post(url, postBody, (call, res) -> {
                    String body = res.body().string();
                    T responseData = (T) jsonAdapter.fromJson(body);
                    success.onSuccess(responseData);
                },
                (call, e) -> {
                    failure.onFailure(call, e);
                });
    }

    public void putJSON(String url, T data, SuccessCallback<T> success, FailureCallback failure) {
        String putBody = jsonAdapter.toJson((T) data);

        put(url, putBody, (call, res) -> {
                    String body = res.body().string();
                    Log.d("Todo", body);
                    T responseData = (T) jsonAdapter.fromJson(body);
                    success.onSuccess(responseData);
                },
                (call, e) -> {
                    failure.onFailure(call, e);
                });
    }

    public void deleteJSON(String url, SuccessCallback<T> success, FailureCallback failure) {
        delete(url, (call, res) -> {
                    success.onSuccess(null);
                },
                (call, e) -> {
                    failure.onFailure(call, e);
                });
    }
}
