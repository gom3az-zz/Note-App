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

    //// TODO: 5/29/2018 add on click listener to edit note
    @Override
    public boolean onItemClick(int position) {
        Toast.makeText(mView, "clicked"+ position, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void updateRecyclerViewData(List<DataModel> newData) {
        mView.notesRecyclerViewAdapter.setAll(newData);
        mView.notesRecyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void openDialog() {
        FragmentManager fm = mView.getSupportFragmentManager();
        NoteDialog noteDialog = new NoteDialog();
        fm.beginTransaction()
                .add(noteDialog.getId(), noteDialog, NoteDialog.class.getName())
                .commit();
    }

    @Override
    public void addNote(DataModel newNote) {
        data.addNote(newNote);
        data.updateDataSet();
    }

}

