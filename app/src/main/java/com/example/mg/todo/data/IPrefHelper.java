package com.example.mg.todo.data;

import com.example.mg.todo.data.model.NoteModel;

import java.util.List;

public interface IPrefHelper {
    void saveData(List<NoteModel> model);

    List<NoteModel> getSavedData();
}
