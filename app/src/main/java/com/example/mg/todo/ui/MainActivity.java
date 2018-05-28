package com.example.mg.todo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.DataModel;
import com.example.mg.todo.ui.contract.ITodoContract;
import com.example.mg.todo.ui.presenter.TodoPresenter;
import com.example.mg.todo.utils.NoteDialog;
import com.example.mg.todo.utils.NotesRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ITodoContract.IView
        , NotesRecyclerViewAdapter.OnItemLongClickListener, NoteDialog.ISendNoteObject {

    @BindView(R.id.todoList)
    RecyclerView todoList;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

    private TodoPresenter mPresenter;
    public NotesRecyclerViewAdapter notesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPresenter = new TodoPresenter(this, sharedPreferences);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void init(List<DataModel> set) {
        todoList.addItemDecoration(new DividerItemDecoration(
                todoList.getContext(),
                DividerItemDecoration.VERTICAL));
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(this, set);
        todoList.setAdapter(notesRecyclerViewAdapter);

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return mPresenter.onItemLongClick(position);
    }

    @Override
    public void send(DataModel newNote) {
        mPresenter.addNote(newNote);
    }

    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {
        mPresenter.openDialog();
    }
}
