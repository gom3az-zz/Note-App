package com.example.mg.todo.ui.NotesActivity.DI;

import com.example.mg.todo.IAppComponent;
import com.example.mg.todo.ui.NotesActivity.NotesActivity;

import dagger.Component;
@INoteActivityScope
@Component(modules = NoteActivityModule.class, dependencies = IAppComponent.class)
public interface INoteActivityComponent {

    void inject(NotesActivity notesActivity);

}
