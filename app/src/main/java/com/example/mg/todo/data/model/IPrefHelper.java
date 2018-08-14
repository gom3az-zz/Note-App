package com.example.mg.todo.data.model;

import java.util.List;

public interface IPrefHelper {
    void saveData(List<NoteModel> model);

    List<NoteModel> getSavedData();
}
