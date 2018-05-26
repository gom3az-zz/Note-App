package com.example.mg.todo;

import android.content.SharedPreferences;
import android.view.KeyEvent;

public class TodoPresenter implements ITodoContract.IPresenter {
    private MainActivity mView;
    private Data data;

    TodoPresenter(MainActivity mView, SharedPreferences sharedPreferences) {
        this.mView = mView;
        data = new Data(this, sharedPreferences);

        mView.init(data.set, data.todoArrayList);

    }

    @Override
    public boolean onItemLongClick(int position) {
        data.todoArrayList.remove(position);
        data.addAll();
        mView.adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onKey(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    data.todoArrayList.add(mView.textEnter.getText().toString());
                    data.addAll();
                    mView.adapter.notifyDataSetChanged();
                    mView.textEnter.setText("");
                    return true;
                default:
                    break;
            }
        }
        return false;
    }
}

