package com.example.mg.todo;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class DataProvider {
    private SharedPreferences.Editor editor;
    private TodoPresenter mPresenter;

    List<String> set = new LinkedList<>();
    private Set<String> savedData = new HashSet<>();

    DataProvider(SharedPreferences sharedPreferences, TodoPresenter todoPresenter) {
        editor = sharedPreferences.edit();
        mPresenter = todoPresenter;
        Set<String> toto = sharedPreferences.getStringSet("toto", null);
        if (toto != null) set.addAll(toto);
    }

    void updateDataSet() {
        savedData.clear();
        savedData.addAll(set);
        editor.putStringSet("toto", savedData).apply();
        mPresenter.updateRecyclerViewData(set);
    }
}

