package com.example.mg.todo.data;

import android.content.SharedPreferences;

import com.example.mg.todo.data.model.DataModel;
import com.example.mg.todo.ui.presenter.TodoPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private SharedPreferences.Editor editor;
    private TodoPresenter mPresenter;
    private final String KEY_NEW_NOTE = "NEW_NOTE2";
    private Gson gson = new Gson();
    private String json;
    private List<DataModel> mDataModels;

    public DataProvider(SharedPreferences sharedPreferences, TodoPresenter todoPresenter) {
        editor = sharedPreferences.edit();
        mPresenter = todoPresenter;
        Type type = new TypeToken<List<DataModel>>() {
        }.getType();

        json = sharedPreferences.getString(KEY_NEW_NOTE, "");
        mDataModels = gson.fromJson(json, type);

    }

    public void updateDataSet() {
        json = gson.toJson(mDataModels);
        editor.putString(KEY_NEW_NOTE, json).apply();
        mPresenter.updateRecyclerViewData(mDataModels);

    }

    public void remove(int position) {
        mDataModels.remove(position);
    }

    public List<DataModel> getDataModels() {
        try {
            return mDataModels;
        } catch (NullPointerException ignored) {
            return new ArrayList<>(0);
        }
    }

    public void addNote(DataModel newNote) {
        try {
            mDataModels.add(newNote);

        } catch (NullPointerException e) {
            mDataModels = new ArrayList<>();
            mDataModels.add(newNote);
        }
    }
}

