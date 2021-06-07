package ch.bbc.navigation.services;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import ch.bbc.navigation.models.Todo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPService {
    private static MediaType jsonMediaType = MediaType.parse("application/json");

    public static interface FailureCallback {
        public void onFailure(@NotNull Call call, @NotNull IOException e);
    }

    public static interface SuccessCallback {
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException;
    }

    private OkHttpClient client = new OkHttpClient();

    public void get(String url, SuccessCallback success, FailureCallback failure) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                failure.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    failure.onFailure(call, new IOException("Error in request to " + url));
                }
                success.onResponse(call, response);
            }
        });
    }

    public void post(String url, String json, SuccessCallback success, FailureCallback failure) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(json, jsonMediaType))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                failure.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    failure.onFailure(call, new IOException("Error in request to " + url));
                }
                success.onResponse(call, response);
            }
        });
    }

    public void put(String url, String json, SuccessCallback success, FailureCallback failure) {
        Log.d("Todo", json);
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(json, jsonMediaType))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                failure.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    failure.onFailure(call, new IOException(response.toString()));
                }
                success.onResponse(call, response);
            }
        });
    }

    public void delete(String url, SuccessCallback success, FailureCallback failure) {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                failure.onFailure(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    failure.onFailure(call, new IOException("Error in request to " + url + " " + response.message()));
                }
                success.onResponse(call, response);
            }
        });
    }

}
