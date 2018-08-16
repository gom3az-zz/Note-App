package com.example.mg.todo.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mg.todo.DI.IAppContext;
import com.example.mg.todo.DI.IAppScope;
import com.example.mg.todo.data.model.NoteModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

@IAppScope
public class PrefrenceHelper implements IPrefHelper {

    private static final String KEY_SHARED_PREF = "todo_app7";
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private String json;

    @Inject
    PrefrenceHelper(@IAppContext Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    @Override
    public void saveData(List<NoteModel> model) {
        json = gson.toJson(model);
        sharedPreferences.edit().putString(KEY_SHARED_PREF, json).apply();
    }

    @Override
    public List<NoteModel> getSavedData() {
        Type type = new TypeToken<List<NoteModel>>() {
        }.getType();

        json = sharedPreferences.getString(KEY_SHARED_PREF, "");
        return gson.fromJson(json, type);
    }
}
