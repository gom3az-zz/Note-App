package com.example.mg.todo.ui.NoteFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.utils.BitmapUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class NoteFragmentPresenter implements INoteFragContract.IPresenter {
    private static final int REQUEST_CODE = 19;
    private NoteFragment mView;
    private NoteModel mNote;
    private String mFileLocation;
    private Bitmap mBitmap;

    NoteFragmentPresenter(NoteFragment mView, NoteModel data) {
        this.mView = mView;
        this.mNote = data;
    }


    @Override
    public void initFragmentData() {
        // if user clicked on a note to update it it calls this function to update the ui of the fragment
        // else init a new note object
        if (mNote != null) {
            mView.editTextTitle.setText(mNote.getText());
            mView.editTextDescription.setText(mNote.getDescription());
            if (mNote.getImage() != null) {
                byte[] bytes = BitmapUtil.decodeImage(mNote.getImage());
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mView.editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null,
                        null,
                        null,
                        new BitmapDrawable(mView.getResources(), mBitmap));
                mView.editTextDescription.setOnTouchListener(mView);
            }
        } else mNote = new NoteModel();
    }

    @Override
    public void onDoneClick() {
        String title = mView.editTextTitle.getText().toString();
        String description = mView.editTextDescription.getText().toString();
        if (title.equals("") || description.equals("")) mView.onFilledDataError();
        else {
            mNote.setText(title);
            mNote.setDescription(description);
            if (mView.mUpdated != -1) // check if the note is newly added or an edited one
                mNote.setmDate(String.format("Edited on: %s",
                        new SimpleDateFormat("EEE, MMM d, ''yy hh:mm aaa",
                                Locale.getDefault()).format(new Date())));
            else
                mNote.setmDate(new SimpleDateFormat("EEE, MMM d, ''yy hh:mm aaa",
                        Locale.getDefault()).format(new Date()));

            // check if there is an image on edit text to add it into database
            if (mView.editTextDescription.getCompoundDrawables()[3] != null)
                mNote.setImage(BitmapUtil.encodedImage(mBitmap));
            mView.mSendNote.sendNoteObject(mNote, mView.mUpdated);
            mView.getDialog().dismiss();
        }
    }

    @Override
    public void onTakeImageClick() {
        try {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = BitmapUtil.createTempImageFile(Objects.requireNonNull(mView.getActivity()));
            mFileLocation = BitmapUtil.mCurrentPhotoPath;
            Uri uri = FileProvider.getUriForFile(mView.getActivity(), "com.example.mg.todo", file);
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            mView.startActivityForResult(i, REQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mBitmap = BitmapUtil.resamplePic(mFileLocation);
            mView.editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null,
                    null,
                    null,
                    new BitmapDrawable(mView.getResources(), mBitmap));
            mView.editTextDescription.setOnTouchListener(mView);
        } else mView.onCancelImageCapture();
    }

    @Override
    public void onImageClick(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //deletes image if user clicked delete button from menu
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete) {
                    mView.editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null,
                            null,
                            null,
                            null);
                    mNote.setImage(null);
                    mView.editTextDescription.setOnTouchListener(null);
                }
                return true;
            }
        });
        popup.show();
    }

}

