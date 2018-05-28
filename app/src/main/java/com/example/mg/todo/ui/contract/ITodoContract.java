package com.example.mg.todo.ui.contract;

import android.view.KeyEvent;

import com.example.mg.todo.data.model.DataModel;

import java.util.List;

public interface ITodoContract {
    interface IView {
        void init(List<DataModel> set);
    }

    interface IPresenter {

        boolean onItemLongClick(int position);

        boolean onKey(int keyCode, KeyEvent event);

        void updateRecyclerViewData(List<DataModel> newData);
    }
}
