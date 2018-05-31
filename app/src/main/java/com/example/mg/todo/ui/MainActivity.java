package com.example.mg.todo.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.contract.ITodoContract;
import com.example.mg.todo.ui.presenter.TodoPresenter;
import com.example.mg.todo.utils.NoteDialog;
import com.example.mg.todo.utils.NotesRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements ITodoContract.IView,
        NotesRecyclerViewAdapter.OnItemLongClickListener,
        NotesRecyclerViewAdapter.OnItemClickListener,
        NoteDialog.ISendNoteObject {

    @BindView(R.id.todoList)
    public
    RecyclerView todoList;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

    private TodoPresenter mPresenter;
    public MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPresenter = new TodoPresenter(this, sharedPreferences);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        menuItem = menu.findItem(R.id.delete);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        todoList.setAdapter(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  RefWatcher refWatcher = App.getRefWatcher(getBaseContext());
        refWatcher.watch(this);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                mPresenter.removeNotes();
                break;

        }
        return true;
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return mPresenter.onItemLongClick(position);
    }

    @Override
    public void onItemClicked(int position) {
        mPresenter.onItemClick(position);
    }

    @Override
    public void send(NoteModel newNote, int mUpdated) {
        mPresenter.addNote(newNote, mUpdated);
    }

    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {
        // null , -1 are passed to create new note object
        mPresenter.openDialog(null, -1);
    }
}
