package com.example.mg.todo;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class DataProvider {
    private SharedPreferences.Editor editor;
    private TodoPresenter mPresenter;
    private final String MyObject = "MyObject1";
    private Gson gson = new Gson();
    private String json;
    private List<DataModel> mDataModels;

    DataProvider(SharedPreferences sharedPreferences, TodoPresenter todoPresenter) {
        editor = sharedPreferences.edit();
        mPresenter = todoPresenter;
        Type type = new TypeToken<List<DataModel>>() {
        }.getType();

        json = sharedPreferences.getString(MyObject, "");
        mDataModels = gson.fromJson(json, type);

    }

    void updateDataSet() {
        json = gson.toJson(mDataModels);
        editor.putString(MyObject, json).apply();
        mPresenter.updateRecyclerViewData(mDataModels);
    }

    public void add(String s) {
        DataModel newObj = new DataModel(s);
        mDataModels.add(newObj);
    }

    void remove(int position) {
        mDataModels.remove(position);
    }

    List<DataModel> getDataModels() {
        try {
            return mDataModels;
        } catch (NullPointerException ignored) {
            return new ArrayList<>(0);
        }
    }

}

