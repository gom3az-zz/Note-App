package com.example.mg.todo.UI.NotesActivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.example.mg.todo.App
import com.example.mg.todo.R
import com.example.mg.todo.UI.NoteFragment.NoteFragment
import com.example.mg.todo.data.model.NoteModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class NotesActivity : AppCompatActivity(), NotesRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: NotesViewModel by lazy {
        ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private val mNotesAdapter: NotesRecyclerViewAdapter by lazy {
        NotesRecyclerViewAdapter(this@NotesActivity, Glide.with(applicationContext), mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        todoList.itemAnimator = DefaultItemAnimator()
        todoList.adapter = mNotesAdapter

        floatingActionButton.setOnClickListener { onNoteClicked(-1, null) }
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadData()

        viewModel.loadResult.observe(this@NotesActivity, Observer {
            val notes = it ?: return@Observer
            mNotesAdapter.swapData(notes.toMutableList())
        })

        viewModel.updateResult.observe(this@NotesActivity, Observer {
            val result = it ?: return@Observer
            result.added?.let { onAdd() }
            result.updated?.let { onUpdate(note = result.updated) }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = App.getRefWatcher(baseContext)
        todoList.adapter = null
        refWatcher.watch(this)
    }

    override fun onItemClicked(position: Int, model: NoteModel) {
        onNoteClicked(position, model)
    }

    private fun onNoteClicked(position: Int, model: NoteModel?) {
        val fm: FragmentManager = supportFragmentManager
        val noteFragment = NoteFragment()
        noteFragment.setModel(position, model)
        fm.beginTransaction()
                .add(noteFragment.id, noteFragment, NoteFragment::class.java.name)
                .commit()

    }

    private fun onAdd() {
        mainLayout.snack("Note added successfully!")
    }

    private fun onUpdate(note: NoteModel) {
        mNotesAdapter.notifyItemChanged(note.id.toInt() - 1)//database id starts from 1
        mainLayout.snack("Note updated successfully!")

    }

    fun View.snack(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }
}