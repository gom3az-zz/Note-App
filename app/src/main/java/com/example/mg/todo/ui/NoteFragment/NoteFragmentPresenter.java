package com.example.mg.todo.ui.NoteFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

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

@SuppressLint("ClickableViewAccessibility")
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

    //todo fix image when rotating gone
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
                mView.onImageAdded(mBitmap, 2f);
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
            // else if user took an image and then delete it so we delete it
            if (mView.imageNote.getDrawable() != null)
                mNote.setImage(BitmapUtil.encodedImage(mBitmap));
            else mNote.setImage(null);
            mView.mSendNote.sendNoteObject(mNote, mView.mUpdated);
            mView.getDialog().dismiss();
        }
    }

    @Override
    public void onTakeImageClick() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(Objects.requireNonNull(mView.getActivity()).getPackageManager()) != null) {
            File file = null;
            try {
                file = BitmapUtil.createTempImageFile(Objects.requireNonNull(mView.getActivity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) {

                mFileLocation = BitmapUtil.mCurrentPhotoPath;
                Uri uri = FileProvider.getUriForFile(mView.getActivity(), "com.example.mg.todo", file);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                mView.startActivityForResult(i, REQUEST_CODE);
            }
        }
    }

    // get user taken image and puts it into description edit text
    // then add to note object
    // compress  mBitmap to byte array using mBitmap CompressFormat
    // then encode it to base 64 to store it as a string into the database
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mBitmap = BitmapUtil.resamplePic(mFileLocation);
            mView.onImageAdded(mBitmap, 1f);
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
                    onDeleteImageClicked();
                } else if (item.getItemId() == R.id.view) {
                    onViewImageClicked();
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onDeleteImageClicked() {
        mView.imageNote.setOnClickListener(null);
        mView.imageNote.setImageDrawable(null);
    }

    @Override
    public void onViewImageClicked() {
        final Dialog builder = new Dialog(Objects.requireNonNull(mView.getContext()));
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(builder.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(R.layout.image_viewer);
        ImageView imageView = builder.findViewById(R.id.image_preview);
        imageView.setImageBitmap(BitmapUtil.resize(mBitmap, 4f));
        builder.show();
    }

}

