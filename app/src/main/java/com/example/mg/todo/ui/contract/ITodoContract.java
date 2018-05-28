package com.example.mg.todo.ui.contract;

import com.example.mg.todo.data.model.DataModel;

import java.util.List;

public interface ITodoContract {
    interface IView {
        void init(List<DataModel> set);
    }

    interface IPresenter {

        boolean onItemLongClick(int position);

        void openDialog();

        void addNote(DataModel newNote);

        void updateRecyclerViewData(List<DataModel> newData);

        boolean onItemClick(int position);
    }
}

