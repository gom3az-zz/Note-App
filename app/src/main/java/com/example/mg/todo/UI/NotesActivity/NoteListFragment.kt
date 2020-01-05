package com.example.mg.todo.UI.NotesActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.example.mg.todo.R
import com.example.mg.todo.UI.NoteFragment.NoteFragment
import com.example.mg.todo.data.model.NoteModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_note_list.*

class NoteListFragment : Fragment(), NotesRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: NotesViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(NotesViewModel::class.java)
    }

    private val mNotesAdapter: NotesRecyclerViewAdapter by lazy {
        NotesRecyclerViewAdapter(this, Glide.with(requireContext()), mutableListOf())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        todoList.itemAnimator = DefaultItemAnimator()
        todoList.adapter = mNotesAdapter

        floatingActionButton.setOnClickListener { onNoteClicked(null) }

        viewModel.loadData()

        viewModel.loadResult.observe(this@NoteListFragment, Observer {
            val notes = it ?: return@Observer
            mNotesAdapter.swapData(notes.toMutableList())

            if (notes.isEmpty()) {
                empty.visibility = View.VISIBLE
            } else {
                empty.visibility = View.GONE
            }
        })

        viewModel.updateResult.observe(this@NoteListFragment, Observer {
            val result = it ?: return@Observer
            result.added?.let { onAdd() }
            result.removed?.let { onRemoved() }
            result.updated?.let { onUpdate() }
        })
    }

    override fun onItemClicked(model: NoteModel) {
        onNoteClicked(model)
    }

    private fun onNoteClicked(model: NoteModel?) {
        val arguments = Bundle()
        model?.let { arguments.putLong(NoteFragment.NOTE_ID, it.id) }
                ?: arguments.putLong(NoteFragment.NOTE_ID, -1)
        findNavController().navigate(R.id.noteFragmentDialog, arguments)

    }

    private fun onAdd() {
        mainLayout.snack("Note added successfully!")
    }

    private fun onUpdate() {
        mainLayout.snack("Note updated successfully!")

    }

    private fun onRemoved() {
        mainLayout.snack("Note deleted successfully!")

    }

    private fun View.snack(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

}
