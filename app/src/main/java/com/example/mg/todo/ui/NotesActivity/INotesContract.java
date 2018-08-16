package com.example.mg.todo.ui.NotesActivity;

import com.example.mg.todo.data.model.NoteModel;

import java.util.List;

public interface INotesContract {
    interface IView {
        void noteRemoved();

        void noteAdded();

        void noteUpdated();
    }

    interface IPresenter {

        void onItemClick(int position);

        void onNoteClick(NoteModel note, int position);

        void onNoteDoneClick(NoteModel newNote, int mUpdated);

        void onResume();


        void onStop();

        void onAdd(NoteModel position);

        void onUpdate(NoteModel newNote, int position);

    }

    interface IDate {
        void updateDataSet();

        void addNote(NoteModel newNote);

        void updateNote(NoteModel newNote, int mUpdated);

        NoteModel getNote(int position);

        List<NoteModel> getDataModels();
    }
}

