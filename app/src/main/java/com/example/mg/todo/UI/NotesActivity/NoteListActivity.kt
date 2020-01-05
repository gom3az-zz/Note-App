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

class NoteListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        val refWatcher = App.getRefWatcher(baseContext)
        refWatcher.watch(this)
    }
}