package ch.bbc.navigation.views.todos.tododetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ch.bbc.navigation.databinding.TodoDetailBinding;
import ch.bbc.navigation.services.TodoService;


public class TodoDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "id";
    private int todoId;
    private TodoDetailBinding binding;
    private TodoService todoService;

    public TodoDetailFragment() {
    }

    public static TodoDetailFragment newInstance(int id) {
        TodoDetailFragment fragment = new TodoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        todoService = new TodoService();
        todoId = getArguments().getInt(ARG_PARAM1);
        binding = TodoDetailBinding.inflate(LayoutInflater.from(getContext()));
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        todoService.getTodo(todoId, (todo) -> binding.setTodo(todo));
    }
}