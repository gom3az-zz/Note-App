package com.example.mg.todo.ui.presenter;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.example.mg.todo.data.DataProvider;
import com.example.mg.todo.data.model.DataModel;
import com.example.mg.todo.ui.MainActivity;
import com.example.mg.todo.ui.contract.ITodoContract;
import com.example.mg.todo.utils.NoteDialog;

import java.util.List;

public class TodoPresenter implements ITodoContract.IPresenter {
    private MainActivity mView;
    private DataProvider data;

    public TodoPresenter(MainActivity mView, SharedPreferences sharedPreferences) {
        this.mView = mView;
        data = new DataProvider(sharedPreferences, this);
        mView.init(data.getDataModels());
    }

    @Override
    public boolean onItemLongClick(int position) {
        data.remove(position);
        data.updateDataSet();
        return true;
    }

    //// TODO: 5/29/2018 update on click listener to edit note with more stuff
    @Override
    public boolean onItemClick(int position) {
        Toast.makeText(mView, "clicked" + position, Toast.LENGTH_SHORT).show();
        DataModel note = data.getNote(position);
        openDialog(note, position);
        return true;
    }


    @Override
    public void updateRecyclerViewData(List<DataModel> newData) {
        mView.notesRecyclerViewAdapter.setAll(newData);
        mView.notesRecyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void openDialog(DataModel note, int position) {
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
    public void addNote(DataModel newNote, int mUpdated) {
        if (mUpdated != -1) data.remove(mUpdated);
        data.addNote(newNote, mUpdated);
        data.updateDataSet();
    }

}

