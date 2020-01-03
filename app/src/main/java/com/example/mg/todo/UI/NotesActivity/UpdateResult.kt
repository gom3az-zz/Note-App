package com.example.mg.todo.UI.NotesActivity

import com.example.mg.todo.data.model.NoteModel

data class UpdateResult(
        val added: NoteModel? = null,
        val updated: NoteModel? = null
)
