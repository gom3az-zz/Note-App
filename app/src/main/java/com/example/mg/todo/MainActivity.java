package com.example.mg.todo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements ITodoContract.IView,
        EditText.OnKeyListener, ListView.OnItemLongClickListener {

    private TodoPresenter mPresenter;
    EditText textEnter;
    ListView todoList;
    SharedPreferences sharedPreferences;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("com.example.mg.todo", MODE_PRIVATE);

        textEnter = findViewById(R.id.textEnter);
        todoList = findViewById(R.id.todoList);

        todoList.setOnItemLongClickListener(this);
        textEnter.setOnKeyListener(this);

        mPresenter = new TodoPresenter(this, sharedPreferences);

    }


    @Override
    public void init(Collection<? extends String> set, ArrayList<String> todoArrayList) {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoArrayList);
        if (set != null) {
            todoArrayList.clear();
            todoArrayList.addAll(set);
        }

        todoList.setAdapter(adapter);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return mPresenter.onKey(keyCode, event);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return mPresenter.onItemLongClick(position);
    }
}
