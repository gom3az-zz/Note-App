package com.example.mg.todo.data

import android.app.Application
import com.example.mg.todo.data.model.NoteModel

class DataProvider(application: Application) {

    private val databaseHelper: INotesDao = DatabaseHelper.invoke(application).notesDao()

    suspend fun addNote(model: NoteModel): Long {
        return databaseHelper.insertNote(model)
    }

    suspend fun updateNote(model: NoteModel): Int {
        return databaseHelper.updateNote(model)
    }

    suspend fun removeNote(id: NoteModel): Int {
        return databaseHelper.deleteNote(id)
    }

    suspend fun dataModels(): List<NoteModel> {
        return databaseHelper.getAllNotes()
    }

}