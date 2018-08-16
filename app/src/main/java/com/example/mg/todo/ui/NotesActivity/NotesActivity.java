package com.example.mg.todo.ui.NotesActivity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.example.mg.todo.App;
import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NoteFragment.NoteFragment;
import com.example.mg.todo.ui.NotesActivity.DI.DaggerINoteActivityComponent;
import com.example.mg.todo.ui.NotesActivity.DI.NoteActivityModule;
import com.squareup.leakcanary.RefWatcher;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotesActivity extends AppCompatActivity
        implements INotesContract.IView,
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

    @Inject
    NotesPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        DaggerINoteActivityComponent.builder()
                .iAppComponent(App.get(this).geAppComponent())
                .noteActivityModule(new NoteActivityModule(this))
                .build().inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getBaseContext());
        refWatcher.watch(this);
    }

    @Override
    public void onItemClicked(int position) {
        mPresenter.onItemClick(position);
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
    public void noteRemoved() {
        String message = "Note removed successfully!";
        Snackbar.make(relativeLayout, String.format(Locale.getDefault(), "%s", message),
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
