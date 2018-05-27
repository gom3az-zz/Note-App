package com.example.mg.todo;

import android.content.SharedPreferences;
import android.view.KeyEvent;

import java.util.List;

public class TodoPresenter implements ITodoContract.IPresenter {
    private MainActivity mView;
    private DataProvider data;

    TodoPresenter(MainActivity mView, SharedPreferences sharedPreferences) {
        this.mView = mView;
        data = new DataProvider(sharedPreferences, this);
        mView.init(data.set);

    }

    @Override
    public boolean onItemLongClick(int position) {
        data.set.remove(position);
        data.updateDataSet();
        return true;
    }

    @Override
    public boolean onKey(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    data.set.add(mView.textEnter.getText().toString());
                    data.updateDataSet();
                    mView.textEnter.setText("");
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public void updateRecyclerViewData(List<String> newData) {
        mView.notesRecyclerViewAdapter.setAll(newData);
        mView.notesRecyclerViewAdapter.notifyDataSetChanged();
    }
}

