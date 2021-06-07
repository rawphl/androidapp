package ch.bbc.navigation.services;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import ch.bbc.navigation.models.Todo;

public class TodoService extends RESTService<Todo> {
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/todos";
    private static final String TAG = "TodoService";

    public static interface TodoCallback {
        public void call(Todo todo);
    }

    public static interface TodosCallback {
        public void call(List<Todo> todos);
    }

    public TodoService() {
        super(Todo.class);
    }

    public void getTodos(TodosCallback fn) throws IOException {
            getJSONArray(BASE_URL, (todos) -> {
                Log.d(TAG, todos.toString());
                fn.call(todos);
            }, (c, e) -> {
                Log.d(TAG, e.getMessage());
            });
    }

    public void getTodo(int id, TodoCallback fn) {
        getJSON(BASE_URL + "/" + id, (todo) -> {
            Log.d(TAG, todo.toString());
            fn.call(todo);
        }, (c, e) -> {
            Log.d(TAG, e.getMessage());
        });
    }

    public void createTodo(Todo newTodo, TodoCallback fn) {
        postJSON(BASE_URL, newTodo, (todo) -> {
            Log.d(TAG, todo.toString());
            fn.call(todo);
        }, (c, e) -> {
            Log.d(TAG, e.getMessage());
        });
    }

    public void updateTodo(Todo newTodo, TodoCallback fn) {
        putJSON(BASE_URL + "/" + newTodo.getId(), newTodo, (todo) -> {
            Log.d(TAG, todo.toString());
            fn.call(todo);
        }, (c, e) -> {
            Log.d(TAG, e.getMessage());
        });
    }

    public void deleteTodo(Todo newTodo, TodoCallback fn) {
        deleteJSON(BASE_URL + "/" + newTodo.getId(), (todo) -> {
            fn.call(todo);
        }, (c, e) -> {
            Log.d(TAG, e.getMessage());
        });
    }
}
