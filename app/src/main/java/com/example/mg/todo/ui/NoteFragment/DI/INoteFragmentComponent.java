package com.example.mg.todo.ui.NoteFragment.DI;

import com.example.mg.todo.DI.IAppComponent;
import com.example.mg.todo.ui.NoteFragment.NoteFragment;

import dagger.Component;

@IFragmentScope
@Component(modules = NoteFragmentModule.class, dependencies = IAppComponent.class)
public interface INoteFragmentComponent {

    void inject(NoteFragment fragment);

}
