package ch.bbc.navigation.views.todos.todolist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.bbc.navigation.databinding.TodoItemBinding;
import ch.bbc.navigation.models.Todo;


public class TodoListRecyclerViewAdapter extends RecyclerView.Adapter<TodoListRecyclerViewAdapter.ViewHolder> {

    private final List<Todo> todos;
    private TodoListViewModel viewModel;

    public TodoListRecyclerViewAdapter(List<Todo> items, TodoListViewModel viewModel) {
        this.viewModel = viewModel;
        todos = items;
    }

    public void update(List<Todo> items) {
        todos.clear();
        todos.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TodoItemBinding binding = TodoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.setTodo(todos.get(position));
        holder.binding.setViewModel(viewModel);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TodoItemBinding binding;

        public ViewHolder(TodoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}