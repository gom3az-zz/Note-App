package com.example.mg.todo.ui.presenter;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import com.example.mg.todo.data.DataProvider;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.MainActivity;
import com.example.mg.todo.ui.contract.ITodoContract;
import com.example.mg.todo.utils.NoteDialog;
import com.example.mg.todo.utils.NotesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class TodoPresenter implements ITodoContract.IPresenter {
    private MainActivity mView;
    private DataProvider data;
    private NotesRecyclerViewAdapter notesRecyclerViewAdapter;
    private List<Integer> mSelectedNotes;

    public TodoPresenter(MainActivity mView, SharedPreferences sharedPreferences) {
        this.mView = mView;
        data = new DataProvider(sharedPreferences, this);
    }

    @Override
    public boolean onItemLongClick(int position) {
        if (mSelectedNotes == null) mSelectedNotes = new ArrayList<>();
        try {
            if (mSelectedNotes.contains(position))
                mSelectedNotes.remove(position);
            else mSelectedNotes.add(position);
        } catch (IndexOutOfBoundsException e) {
            mSelectedNotes = new ArrayList<>();
        }
        if (mSelectedNotes.size() > 0) mView.menuItem.setVisible(true);
        else mView.menuItem.setVisible(false);
        return true;
    }

    //// TODO: 5/29/2018 update on click listener to edit note with more stuff
    @Override
    public void onItemClick(int position) {
        NoteModel note = data.getNote(position);
        onNoteClick(note, position);
    }

    @Override
    public void updateViewData(List<NoteModel> newData) {
        notesRecyclerViewAdapter.setAll(newData);
        notesRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void initMainRecyclerData() {
        // initMainRecyclerData home recycler view with data saved at shared pref
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(mView, data.getDataModels());
        mView.todoList.setAdapter(notesRecyclerViewAdapter);
        if (mSelectedNotes != null) mSelectedNotes.clear();
    }

    @Override
    public void onNoteClick(NoteModel note, int position) {
        FragmentManager fm = mView.getSupportFragmentManager();
        NoteDialog noteDialog = new NoteDialog();
        noteDialog.setModel(note, position);
        fm.beginTransaction()
                .add(noteDialog.getId(), noteDialog, NoteDialog.class.getName())
                .commit();
    }

    // called when user clicks done button from fragment
    // parameter mUpdated refers to location of the updated note
    // if it equals to -1 then it means its a new note else its an updated note
    // so we remove the old note object before adding the new updated one
    @Override
    public void onNoteDoneClick(NoteModel newNote, int mUpdated) {
        if (mUpdated != -1) data.remove(mUpdated);
        data.addNote(newNote, mUpdated);
        data.updateDataSet();
        mView.menuItem.setVisible(false);
        if (mSelectedNotes != null) mSelectedNotes.clear();

    }

    public void removeNotes() {
        for (int i : mSelectedNotes) {
            data.remove(i);
        }
        mSelectedNotes.clear();
        mView.menuItem.setVisible(false);
        data.updateDataSet();
    }

}

