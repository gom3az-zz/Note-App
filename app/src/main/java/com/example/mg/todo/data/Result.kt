package com.example.mg.todo.data

import com.example.mg.todo.data.model.NoteModel

data class Result(
        val added: NoteModel? = null,
        val updated: NoteModel? = null,
        val removed: NoteModel? = null
)
