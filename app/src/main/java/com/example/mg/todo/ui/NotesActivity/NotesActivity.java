package com.example.mg.todo.ui.NotesActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NoteFragment.NoteFragment;
import com.example.mg.todo.utils.NotesRecyclerViewAdapter;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotesActivity extends AppCompatActivity
        implements INotesContract.IView,
        NotesRecyclerViewAdapter.OnItemLongClickListener,
        NotesRecyclerViewAdapter.OnItemClickListener,
        NoteFragment.ISendNoteObject {

    @BindView(R.id.todoList)
    public
    RecyclerView todoList;
    @BindView(R.id.toolbar)
    public
    Toolbar toolbar;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    private NotesPresenter mPresenter;
    public MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPresenter = new NotesPresenter(this, sharedPreferences);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.initMainRecyclerData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // RefWatcher refWatcher = App.getRefWatcher(getBaseContext());
        // refWatcher.watch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        menuItem = menu.findItem(R.id.delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                mPresenter.onRemoveClicked();
                break;
        }
        return true;
    }

    @Override
    public void onItemClicked(int position) {
        mPresenter.onItemClick(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return mPresenter.onItemLongClick(position);
    }

    @Override
    public void sendNoteObject(NoteModel newNote, int mUpdated) {
        mPresenter.onNoteDoneClick(newNote, mUpdated);
    }

    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {
        // null , -1 are passed to create new note object
        mPresenter.onNoteClick(null, -1);
    }

    @Override
    public void removeMessage(int size) {
        String message = size > 1 ? "Notes were removed!" : "Note was removed!";
        Snackbar.make(relativeLayout, String.format(Locale.getDefault(), "%d %s", size, message),
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void noteAdded() {
        Snackbar.make(relativeLayout, "Note added successfully!",
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void noteUpdated() {
        Snackbar.make(relativeLayout, "Note updated successfully!",
                Snackbar.LENGTH_SHORT)
                .show();
    }
}
