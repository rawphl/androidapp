package ch.bbc.navigation.views.todos.todolist;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import ch.bbc.navigation.R;
import ch.bbc.navigation.models.Todo;

public class TodoListViewModel extends ViewModel {
    private MutableLiveData<List<Todo>> todos;

    public TodoListViewModel() {
        this.todos = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Todo>> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos.postValue(todos);
    }

    public void navigateTo(View view, int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        Navigation.findNavController(view).navigate(R.id.nav_tododetail, args);
    }
}
