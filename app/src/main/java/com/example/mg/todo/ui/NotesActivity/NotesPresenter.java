package com.example.mg.todo.ui.NotesActivity;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;

import com.example.mg.todo.data.DataProvider;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NoteFragment.NoteFragment;
import com.example.mg.todo.ui.NotesActivity.DI.INoteActivityScope;
import com.example.mg.todo.utils.NotesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

@INoteActivityScope
public class NotesPresenter implements INotesContract.IPresenter {
    private final NotesActivity mView;
    private final DataProvider mDataProvider;
    private NotesRecyclerViewAdapter mNotesAdapter;
    private List<String> mSelectedNotes;
    //private static final String TAG = "NotesPresenter";

    @Inject
    NotesPresenter(NotesActivity mView, DataProvider dataProvider, NotesRecyclerViewAdapter adapter) {
        this.mView = mView;
        this.mDataProvider = dataProvider;
        this.mNotesAdapter = adapter;
        mSelectedNotes = new ArrayList<>();
    }

    @Override
    public void onResume() {
        // initFragmentData home recycler view with mDataProvider saved at shared pref
        mNotesAdapter.setData(mDataProvider.getDataModels());
        mView.todoList.setItemAnimator(new DefaultItemAnimator());
        mView.todoList.setAdapter(mNotesAdapter);
    }

    @Override
    public void onStop() {
        mView.todoList.setAdapter(null);
    }

    @Override
    public void onItemClick(int position) {
        NoteModel note = mDataProvider.getNote(position);
        onNoteClick(note, position);
    }

    @Override
    public boolean onItemLongClick(int location) {
        String position = String.valueOf(location);

        if (mSelectedNotes.contains(position)) mSelectedNotes.remove(position);
        else mSelectedNotes.add(position);

        if (mSelectedNotes.size() > 0) mView.menuItem.setVisible(true);
        else mView.menuItem.setVisible(false);
        return true;
    }

    @Override
    public void onNoteClick(NoteModel note, int position) {
        FragmentManager fm = mView.getSupportFragmentManager();
        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setModel(note, position);
        fm.beginTransaction()
                .add(noteFragment.getId(), noteFragment, NoteFragment.class.getName())
                .commit();
    }

    // called when user clicks done button from fragment
    // parameter mUpdated refers to location of the updated note
    // if it equals to -1 then it means its a new note else its an updated note
    @Override
    public void onNoteDoneClick(NoteModel newNote, int mUpdated) {
        if (mUpdated == -1) onAdd(newNote);
        else onUpdate(newNote, mUpdated);

        mView.menuItem.setVisible(false);
        if (mSelectedNotes != null) mSelectedNotes.clear();

    }

    @Override
    public void onRemoveClicked() {
        //removing notes in reverse order for better ui translation
        Collections.sort(mSelectedNotes);
        Collections.reverse(mSelectedNotes);
        for (String str : mSelectedNotes) {
            mDataProvider.removeNote(Integer.valueOf(str));
            mNotesAdapter.notifyItemRemoved(Integer.valueOf(str));
        }
        mView.menuItem.setVisible(false);
        mView.noteRemoved(mSelectedNotes.size());
        mSelectedNotes.clear();
    }

    @Override
    public void onAdd(NoteModel position) {
        mDataProvider.addNote(position);
        mView.noteAdded();
        mNotesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdate(NoteModel newNote, int position) {
        mDataProvider.updateNote(newNote, position);
        mView.noteUpdated();
        mNotesAdapter.notifyItemChanged(position);
    }

}

