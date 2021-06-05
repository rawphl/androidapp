package ch.bbc.navigation.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ch.bbc.navigation.models.Todo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TodoService {
    private static final String TAG = "TodoService";
    public static interface TodoCallback {
        public void call(Todo todo);
    }

    public static interface TodosCallback {
        public void call(List<Todo> todos);
    }

    private OkHttpClient client = new OkHttpClient();
    private Moshi moshi = new Moshi.Builder().build();
    private JsonAdapter<Todo> todoAdapter;
    private JsonAdapter<List<Todo>> todosAdapter;
    private Context context;

    public TodoService(Context context) {
        this.context = context;
        Type type = Types.newParameterizedType(List.class, Todo.class);
        todosAdapter = moshi.adapter(type);
        todoAdapter = moshi.adapter(Todo.class);
    }

    public void loadTodos(TodosCallback fn) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("todos", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("todos", "");

        if(!"".equals(json)) {
            Log.d(TAG, "todos loaded from shared preferences");
            List<Todo> todos = todosAdapter.fromJson(json);
            fn.call(todos);
        } else {
            Request request = new Request.Builder()
                    .url("https://jsonplaceholder.typicode.com/todos")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code $response");
                    }
                    String json = response.body().string();
                    sharedPreferences.edit().putString("todos", json).commit();
                    List<Todo> todos = todosAdapter.fromJson(json);
                    Log.d(TAG, "todos loaded from web");
                    fn.call(todos);
                }
            });
        }

    }

    public void loadTodo(int id, TodoCallback fn) {
        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/todos/" + id)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code $response");
                }
                String json = response.body().string();
                Todo todo = todoAdapter.fromJson(json);
                fn.call(todo);
            }
        });
    }
}
