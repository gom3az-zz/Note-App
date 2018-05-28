package com.example.mg.todo;

import android.view.KeyEvent;

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
