package com.example.mg.todo.data;

import android.content.SharedPreferences;

import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NotesActivity.NotesPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private SharedPreferences.Editor editor;
    private final String KEY_NEW_NOTE = "NEW_NOTE20";
    private NotesPresenter mPresenter;
    private Gson gson = new Gson();
    private String json;
    private List<NoteModel> mNoteModels;
    private DataSetOperations dataSetOperations;

    // TODO: 5/28/2018 change shared pref data storage to sqlite
    // storing array of object as a string into shared pref
    //
    public DataProvider(SharedPreferences sharedPreferences, NotesPresenter notesPresenter) {
        dataSetOperations = notesPresenter;
        editor = sharedPreferences.edit();
        mPresenter = notesPresenter;
        Type type = new TypeToken<List<NoteModel>>() {
        }.getType();

        json = sharedPreferences.getString(KEY_NEW_NOTE, "");
        mNoteModels = gson.fromJson(json, type);

    }

    public void updateDataSet() {
        json = gson.toJson(mNoteModels);
        editor.putString(KEY_NEW_NOTE, json).apply();
    }

    public void addNote(NoteModel newNote) {
        try {
            mNoteModels.add(newNote);
            dataSetOperations.onAdd(mNoteModels.indexOf(newNote));
        } catch (NullPointerException e) {
            // first note
            mNoteModels = new ArrayList<>();
            mNoteModels.add(newNote);
            mPresenter.notesRecyclerViewAdapter.setAll(mNoteModels);
            dataSetOperations.onAdd(mNoteModels.indexOf(newNote));
        }

    }

    public void updateNote(NoteModel newNote, int mUpdated) {
        mNoteModels.set(mUpdated, newNote);
        dataSetOperations.onUpdate(mUpdated);
    }

    public void removeNote(int position) {
        mNoteModels.remove(position);
        dataSetOperations.onRemove(position);
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

    public interface DataSetOperations {

        void onUpdate(int position);

        void onRemove(int position);

        void onAdd(int position);

    }
}

