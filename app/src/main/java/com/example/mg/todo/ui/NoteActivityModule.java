package com.example.mg.todo.ui;


import android.app.Activity;

import com.example.mg.todo.data.DataProvider;
import com.example.mg.todo.ui.NotesActivity.INotesContract;
import com.example.mg.todo.ui.NotesActivity.NotesActivity;
import com.example.mg.todo.ui.NotesActivity.NotesPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NoteActivityModule {
    private final Activity activity;

    public NoteActivityModule(NotesActivity activity) {
        this.activity = activity;
    }

    @Provides
    @INoteActivityScope
    INotesContract.IPresenter presenter(NotesPresenter presenter) {
        return presenter;
    }

    @Provides
    @INoteActivityScope
    INotesContract.IDate date(DataProvider dataProvider) {
        return dataProvider;
    }

    @Provides
    @INoteActivityScope
    NotesActivity activity() {
        return (NotesActivity) activity;
    }

}
