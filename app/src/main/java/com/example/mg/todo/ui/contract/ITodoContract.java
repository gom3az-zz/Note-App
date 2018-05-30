package com.example.mg.todo.ui.contract;

import com.example.mg.todo.data.model.NoteModel;

import java.util.List;

public interface ITodoContract {
    interface IView {
        void init(List<NoteModel> set);
    }

    interface IPresenter {

        boolean onItemLongClick(int position);

        boolean onItemClick(int position);

        void openDialog(NoteModel note, int position);

        void addNote(NoteModel newNote, int mUpdated);

        void updateRecyclerViewData(List<NoteModel> newData);

    }
}

