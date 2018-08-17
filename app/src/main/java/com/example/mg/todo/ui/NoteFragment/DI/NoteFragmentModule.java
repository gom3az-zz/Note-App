package com.example.mg.todo.ui.NoteFragment.DI;


import com.example.mg.todo.ui.NoteFragment.INoteFragContract;
import com.example.mg.todo.ui.NoteFragment.NoteFragment;
import com.example.mg.todo.ui.NoteFragment.NoteFragmentPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NoteFragmentModule {
    private final NoteFragment fragment;

    public NoteFragmentModule(NoteFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @IFragmentScope
    NoteFragment providesContext() {
        return fragment;
    }

    @Provides
    @IFragmentScope
    INoteFragContract.IPresenter providesPresenter(NoteFragmentPresenter presenter) {
        return presenter;
    }

}
