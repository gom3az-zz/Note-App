package com.example.mg.todo.data;

import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NotesActivity.DI.INoteActivityScope;
import com.example.mg.todo.ui.NotesActivity.INotesContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@INoteActivityScope
public class DataProvider implements INotesContract.IDate {
    private PrefrenceHelper prefrenceHelper;
    private List<NoteModel> mNoteModels;

    @Inject
    DataProvider(PrefrenceHelper helper) {
        prefrenceHelper = helper;
    }

    @Override
    public void updateDataSet() {
        prefrenceHelper.saveData(mNoteModels);
    }

    @Override
    public void addNote(NoteModel newNote) {
        mNoteModels.add(newNote);
        updateDataSet();
    }

    @Override
    public void updateNote(NoteModel newNote, int mUpdated) {
        mNoteModels.set(mUpdated, newNote);
        updateDataSet();
    }

    @Override
    public NoteModel getNote(int position) {
        return mNoteModels.get(position);
    }

    @Override
    public List<NoteModel> getDataModels() {
        mNoteModels = prefrenceHelper.getSavedData() != null ?
                prefrenceHelper.getSavedData() : new ArrayList<>();
        return mNoteModels;
    }
}

