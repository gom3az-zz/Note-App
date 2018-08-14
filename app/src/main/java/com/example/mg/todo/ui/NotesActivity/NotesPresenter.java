package com.example.mg.todo.ui.NotesActivity;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;

import com.example.mg.todo.data.DataProvider;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.INoteActivityScope;
import com.example.mg.todo.ui.NoteFragment.NoteFragment;
import com.example.mg.todo.utils.NotesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

@INoteActivityScope
public class NotesPresenter implements INotesContract.IPresenter {
    private final NotesActivity mView;
    private final DataProvider data;
    private NotesRecyclerViewAdapter notesRecyclerViewAdapter;
    private List<String> mSelectedNotes = new ArrayList<>();
    //private static final String TAG = "NotesPresenter";

    @Inject
    NotesPresenter(NotesActivity mView, DataProvider dataProvider) {
        this.mView = mView;
        this.data = dataProvider;
    }

    @Override
    public void initMainRecyclerData() {
        // initFragmentData home recycler view with data saved at shared pref
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(mView, data.getDataModels());
        mView.todoList.setItemAnimator(new DefaultItemAnimator());
        mView.todoList.setAdapter(notesRecyclerViewAdapter);
        if (mView.menuItem != null) mView.menuItem.setVisible(false);
        if (mSelectedNotes != null) mSelectedNotes.clear();
    }

    @Override
    public void onItemClick(int position) {
        NoteModel note = data.getNote(position);
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
        Collections.sort(mSelectedNotes);
        Collections.reverse(mSelectedNotes);
        for (String str : mSelectedNotes) {
            onRemove(Integer.valueOf(str));
        }
        mView.menuItem.setVisible(false);
        mView.noteRemoved(mSelectedNotes.size());
        mSelectedNotes.clear();
    }

    private void onAdd(NoteModel position) {
        data.addNote(position);
        data.updateDataSet();
        notesRecyclerViewAdapter.notifyDataSetChanged();
        mView.noteAdded();
    }

    private void onUpdate(NoteModel newNote, int position) {
        data.updateNote(newNote, position);
        data.updateDataSet();
        mView.noteUpdated();
        notesRecyclerViewAdapter.notifyItemChanged(position);
    }

    private void onRemove(int position) {
        data.removeNote(position);
        data.updateDataSet();
        notesRecyclerViewAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onStop() {
        mView.todoList.setAdapter(null);
        mSelectedNotes = null;
    }
}

