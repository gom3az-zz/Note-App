package com.example.mg.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.mg.todo.utils.NotesRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ITodoContract.IView,
        EditText.OnKeyListener, NotesRecyclerViewAdapter.OnItemLongClickListener {

    @BindView(R.id.textEnter)
    EditText textEnter;
    @BindView(R.id.todoList)
    RecyclerView todoList;

    private TodoPresenter mPresenter;
    SharedPreferences sharedPreferences;
    NotesRecyclerViewAdapter notesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        textEnter.setOnKeyListener(this);

        mPresenter = new TodoPresenter(this, sharedPreferences);

    }


    @Override
    public void init(List<String> set) {

        todoList.addItemDecoration(new DividerItemDecoration(
                todoList.getContext(),
                DividerItemDecoration.VERTICAL));
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(this, set);
        todoList.setAdapter(notesRecyclerViewAdapter);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return mPresenter.onKey(keyCode, event);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return mPresenter.onItemLongClick(position);
    }
}
