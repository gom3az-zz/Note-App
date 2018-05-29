package com.example.mg.todo.ui.contract;

import com.example.mg.todo.data.model.DataModel;

import java.util.List;

public interface ITodoContract {
    interface IView {
        void init(List<DataModel> set);
    }

    interface IPresenter {

        boolean onItemLongClick(int position);

        boolean onItemClick(int position);

        void openDialog(DataModel note, int position);

        void addNote(DataModel newNote, int mUpdated);

        void updateRecyclerViewData(List<DataModel> newData);

    }
}

