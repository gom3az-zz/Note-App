package com.example.mg.todo.ui.presenter;

import android.content.SharedPreferences;
import android.view.KeyEvent;

import com.example.mg.todo.data.DataProvider;
import com.example.mg.todo.ui.contract.ITodoContract;
import com.example.mg.todo.data.model.DataModel;
import com.example.mg.todo.ui.MainActivity;
import java.util.List;

public class TodoPresenter implements ITodoContract.IPresenter {
    private MainActivity mView;
    private DataProvider data;

    public TodoPresenter(MainActivity mView, SharedPreferences sharedPreferences) {
        this.mView = mView;
        data = new DataProvider(sharedPreferences, this);
        mView.init(data.getDataModels());

    }

    @Override
    public boolean onItemLongClick(int position) {
        data.remove(position);
        data.updateDataSet();
        return true;
    }

    @Override
    public boolean onKey(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    data.add(mView.textEnter.getText().toString());
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
    public void updateRecyclerViewData(List<DataModel> newData) {
        mView.notesRecyclerViewAdapter.setAll(newData);
        mView.notesRecyclerViewAdapter.notifyDataSetChanged();
    }
}

