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
            var id: Int = -1
            withContext(Dispatchers.IO) {
                id = dataProvider.removeNote(model)
            }
            if (id > -1) {
                val list = _loadResult.value?.toMutableList()
                list?.removeAll { noteModel -> noteModel.id == model.id }
                _loadResult.value = list
            }
            _updateResult.value = Result(removed = model)

        }
    }

    fun updateNote(model: NoteModel) {
        CoroutineScope(Main).launch {
            var id: Int = -1
            withContext(Dispatchers.IO) {
                id = dataProvider.updateNote(model)
            }
            if (id > -1) {
                val list = _loadResult.value?.toMutableList()
                list?.map { noteModel ->
                    if (noteModel.id == model.id) {
                        noteModel.image = model.image
                        noteModel.date = model.date
                        noteModel.text = model.text
                        noteModel.description = model.description
                    }
                }
                _loadResult.value = list
            }
            _updateResult.value = Result(updated = model)
        }
    }

    fun addNote(model: NoteModel) {
        CoroutineScope(Main).launch {
            var id: Long = -1
            withContext(Dispatchers.IO) {
                id = dataProvider.addNote(model)
            }
            if (id > -1) {
                val list = _loadResult.value?.toMutableList()
                model.id = id
                list?.add(model)
                _loadResult.value = list
            }
            _updateResult.value = Result(added = model)
        }
    }

    fun getNote(id: Long): LiveData<NoteModel> {
        var note: NoteModel? = null
        val liveData = MutableLiveData<NoteModel>()
        CoroutineScope(Main).launch {
            withContext(Dispatchers.IO) {
                note = dataProvider.getNote(id)
            }
            liveData.value = note
        }
        return liveData
    }
}