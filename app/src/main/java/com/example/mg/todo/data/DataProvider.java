package com.example.mg.todo.data;

import android.content.SharedPreferences;

import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.presenter.TodoPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private SharedPreferences.Editor editor;
    private TodoPresenter mPresenter;
    private final String KEY_NEW_NOTE = "NEW_NOTE6";
    private Gson gson = new Gson();
    private String json;
    private List<NoteModel> mNoteModels;

    // TODO: 5/28/2018 change shared pref data storage to sqlite
    // storing array of object as a string into shared pref
    //
    public DataProvider(SharedPreferences sharedPreferences, TodoPresenter todoPresenter) {
        editor = sharedPreferences.edit();
        mPresenter = todoPresenter;
        Type type = new TypeToken<List<NoteModel>>() {
        }.getType();

        json = sharedPreferences.getString(KEY_NEW_NOTE, "");
        mNoteModels = gson.fromJson(json, type);

    }

    public void updateDataSet() {
        json = gson.toJson(mNoteModels);
        editor.putString(KEY_NEW_NOTE, json).apply();
        mPresenter.updateRecyclerViewData(mNoteModels);

    }

    public void remove(int position) {
        try {
            mNoteModels.remove(position);
        } catch (IndexOutOfBoundsException e) {
            mNoteModels = new ArrayList<>();
        }
    }

    // if mUpdated is -1 then it means its a new object
    // if not then it adds the updated note to its same location
    public void addNote(NoteModel newNote, int mUpdated) {
        try {
            if (mUpdated != -1) {
                mNoteModels.add(mUpdated, newNote);
            } else mNoteModels.add(newNote);

        } catch (NullPointerException e) {
            mNoteModels = new ArrayList<>();
            mNoteModels.add(newNote);
        }
    }

    public NoteModel getNote(int position) {
        return mNoteModels.get(position);
    }

    public List<NoteModel> getDataModels() {
        try {
            return mNoteModels;
        } catch (NullPointerException ignored) {
            return new ArrayList<>(0);
        }
    }
}

