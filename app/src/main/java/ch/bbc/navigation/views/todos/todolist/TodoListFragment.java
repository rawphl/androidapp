package ch.bbc.navigation.views.todos.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import ch.bbc.navigation.R;
import ch.bbc.navigation.services.TodoService;

public class TodoListFragment extends Fragment {
    private static final String TAG = "TodoFragment";
    private TodoListViewModel viewModel;
    private TodoListRecyclerViewAdapter todoRecyclerViewAdapter;
    private TodoService todoService;

    public TodoListFragment() {

    }

    public static TodoListFragment newInstance() {
        TodoListFragment fragment = new TodoListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        todoService = new TodoService(getContext());
        View view = inflater.inflate(R.layout.todo_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            viewModel = new ViewModelProvider(requireActivity()).get(TodoListViewModel.class);
            todoRecyclerViewAdapter = new TodoListRecyclerViewAdapter(viewModel.getTodos().getValue(), viewModel);
            recyclerView.setAdapter(todoRecyclerViewAdapter);
            viewModel.getTodos().observe(getViewLifecycleOwner(), (todos) -> {
                todoRecyclerViewAdapter.update(todos);
            });
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "LOADING TODOS");

        try {
            todoService.loadTodos((todos) ->
            {
                viewModel.setTodos(todos);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}