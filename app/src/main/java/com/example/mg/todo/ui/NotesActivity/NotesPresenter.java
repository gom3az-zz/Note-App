package com.example.mg.todo.ui.NotesActivity;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.mg.todo.data.DataProvider;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NoteFragment.NoteFragment;
import com.example.mg.todo.ui.NotesActivity.DI.INoteActivityScope;
import com.example.mg.todo.utils.SwipeHelper;

import javax.inject.Inject;


@INoteActivityScope
public class NotesPresenter implements INotesContract.IPresenter, SwipeHelper.OnItemSwipeListener {
    private final NotesActivity mView;
    private final DataProvider mDataProvider;
    private NotesRecyclerViewAdapter mNotesAdapter;
    //private static final String TAG = "NotesPresenter";

    @Inject
    NotesPresenter(NotesActivity mView, DataProvider dataProvider, NotesRecyclerViewAdapter adapter) {
        this.mView = mView;
        this.mDataProvider = dataProvider;
        this.mNotesAdapter = adapter;
    }

    @Override
    public void onResume() {
        // initFragmentData home recycler view with mDataProvider saved at shared pref
        mNotesAdapter.setData(mDataProvider.getDataModels());
        mView.todoList.setItemAnimator(new DefaultItemAnimator());
        mView.todoList.setAdapter(mNotesAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(new SwipeHelper(0, ItemTouchHelper.LEFT, this));
        helper.attachToRecyclerView(mView.todoList);
    }

    @Override
    public void onStop() {
        mView.todoList.setAdapter(null);
    }

    @Override
    public void onItemClick(int position) {
        NoteModel note = mDataProvider.getNote(position);
        onNoteClick(note, position);
    }

    @Override
    public void onNoteClick(NoteModel note, int position) {
        FragmentManager fm = mView.getSupportFragmentManager();
        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setModel(note, position);
        fm.beginTransaction()
                .add(noteFragment.getId(), noteFragment, NoteFragment.class.getName())
                .commit();
    }

    // called when user clicks done button from fragment
    // parameter mUpdated refers to location of the updated note
    // if it equals to -1 then it means its a new note else its an updated note
    @Override
    public void onNoteDoneClick(NoteModel newNote, int mUpdated) {
        if (mUpdated == -1) onAdd(newNote);
        else onUpdate(newNote, mUpdated);
    }

    @Override
    public void onAdd(NoteModel position) {
        mDataProvider.addNote(position);
        mView.noteAdded();
        mNotesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdate(NoteModel newNote, int position) {
        mDataProvider.updateNote(newNote, position);
        mView.noteUpdated();
        mNotesAdapter.notifyItemChanged(position);
    }

    @Override
    public void onItemSwipe(RecyclerView.ViewHolder viewHolder, int location) {

        mNotesAdapter.removeItem(viewHolder.getAdapterPosition());
        mView.noteRemoved();
        mDataProvider.updateDataSet();
    }

}

