package com.example.mg.todo;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

class Data {
    private TodoPresenter presenter;
    private SharedPreferences sharedPreferences;
    Set<String> set;
    ArrayList<String> todoArrayList;

    Data(TodoPresenter todoPresenter, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.presenter = todoPresenter;
        todoArrayList = new ArrayList<>();
        set = sharedPreferences.getStringSet("todo", null);
    }

    void addAll() {
        set.clear();
        set.addAll(todoArrayList);
        sharedPreferences.edit().putStringSet("todo", set).apply();

    }


}

