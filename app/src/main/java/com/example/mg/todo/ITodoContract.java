package com.example.mg.todo;

import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.Collection;

public interface ITodoContract {
    interface IView {
        void init(Collection<? extends String> set, ArrayList<String> todoArrayList);
    }

    interface IPresenter {

        boolean onItemLongClick(int position);

        boolean onKey(int keyCode, KeyEvent event);
    }
}
