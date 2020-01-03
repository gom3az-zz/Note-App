package com.example.mg.todo.UI.NotesActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mg.todo.data.DataProvider
import com.example.mg.todo.data.model.NoteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataProvider: DataProvider = DataProvider(application)

    private var _loadResult = MutableLiveData<List<NoteModel>>()
    var loadResult: LiveData<List<NoteModel>> = _loadResult

    private var _updateResult = MutableLiveData<UpdateResult>()
    var updateResult: LiveData<UpdateResult> = _updateResult


    fun loadData() {
        CoroutineScope(IO).launch {
            val dataModels = dataProvider.dataModels()
            withContext(Main) {
                _loadResult.value = dataModels
            }
        }
    }

    fun removeNote(model: NoteModel) {
        CoroutineScope(IO).launch {
            dataProvider.removeNote(model)
            loadData()

            withContext(Main) {
                _updateResult.value = UpdateResult(removed = model)
            }
        }
    }

    fun updateNote(model: NoteModel) {
        CoroutineScope(IO).launch {
            dataProvider.updateNote(model)
            loadData()

            withContext(Main) {
                _updateResult.value = UpdateResult(updated = model)
            }
        }
    }

    fun addNote(model: NoteModel) {
        CoroutineScope(IO).launch {
            dataProvider.addNote(model)
            loadData()

            withContext(Main) {
                _updateResult.value = UpdateResult(added = model)
            }
        }
    }
}