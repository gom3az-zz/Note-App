package com.example.mg.todo.ui.presenter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.mg.todo.data.DataProvider;
import com.example.mg.todo.data.model.DataModel;
import com.example.mg.todo.ui.MainActivity;
import com.example.mg.todo.ui.contract.ITodoContract;
import com.example.mg.todo.utils.NoteDialog;

import java.util.List;
import java.util.Objects;

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

    @Override
    public void updateRecyclerViewData(List<DataModel> newData) {
        mView.notesRecyclerViewAdapter.setAll(newData);
        mView.notesRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void openDialog() {
        NoteDialog cdd = new NoteDialog(mView);
        Objects.requireNonNull(cdd.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    @Override
    public void addNote(DataModel newNote) {
        data.addNote(newNote);
        data.updateDataSet();
    }

}

