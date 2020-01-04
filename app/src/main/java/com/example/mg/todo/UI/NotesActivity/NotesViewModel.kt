package com.example.mg.todo.UI.NotesActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mg.todo.data.DataProvider
import com.example.mg.todo.data.Result
import com.example.mg.todo.data.model.NoteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataProvider: DataProvider = DataProvider(application)

    private var _loadResult = MutableLiveData<List<NoteModel>>()
    var loadResult: LiveData<List<NoteModel>> = _loadResult

    private var _updateResult = MutableLiveData<Result>()
    var updateResult: LiveData<Result> = _updateResult


    fun loadData() {
        CoroutineScope(Main).launch {
            val notes = withContext(Dispatchers.IO) { dataProvider.dataModels() }
            _loadResult.value = notes
        }
    }

    fun removeNote(model: NoteModel) {
        CoroutineScope(Main).launch {
            var removedNote: Int = -1

            withContext(Dispatchers.IO) {
                removedNote = dataProvider.removeNote(model)
            }
            _updateResult.value = Result(removed = model)

            if (removedNote > -1) {
                val list = _loadResult.value?.toMutableList()
                list?.remove(model)
                _loadResult.value = list
            }
        }
    }

    fun updateNote(model: NoteModel) {
        CoroutineScope(Main).launch {
            var updatedNote: Int = -1
            withContext(Dispatchers.IO) {
                updatedNote = dataProvider.updateNote(model)
            }
            _updateResult.value = Result(updated = model)

            if (updatedNote > -1) {
                _loadResult.value = loadResult.value
            }
        }
    }

    fun addNote(model: NoteModel) {
        CoroutineScope(Main).launch {
            var addedNote: Long = -1
            withContext(Dispatchers.IO) {
                addedNote = dataProvider.addNote(model)

            }
            _updateResult.value = Result(added = model)

            if (addedNote > -1) {
                val list = _loadResult.value?.toMutableList()
                list?.add(model)
                _loadResult.value = list
            }
        }
    }
}