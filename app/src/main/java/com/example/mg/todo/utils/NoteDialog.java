package com.example.mg.todo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class NoteDialog extends android.support.v4.app.DialogFragment
        implements Button.OnClickListener,
        EditText.OnKeyListener,
        EditText.OnTouchListener {

    @BindView(R.id.edit_text_title)
    EditText editTextTitle;
    @BindView(R.id.edit_text_description)
    EditText editTextDescription;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.btn_add_image)
    Button btnAddImage;

    Unbinder unbinder;
    //private static final String TAG = "NoteDialog";
    public static final int REQUEST_CODE = 19;
    private ISendNoteObject mSendNote;
    private Context mActivity;
    private NoteModel mNote;
    private String mFileLocation;
    private int mUpdated = -1;
    private Bitmap mBitmap;

    public NoteDialog() {
    }

    public void setModel(NoteModel model, int position) {
        this.mNote = model;
        mUpdated = position;
    }

    @Override
    public void onAttach(Context context) {
        // casting send note object to parent activity to call its method
        super.onAttach(context);
        this.mActivity = context;
        try {
            mSendNote = (ISendNoteObject) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement send data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_dialog, container, false);
        unbinder = ButterKnife.bind(this, v);
        btnDone.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        editTextDescription.setOnKeyListener(this);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // if user clicked on a note to update it it calls this function to update the ui of the fragment
        // else it init a new note object
        if (mNote != null) {
            initDialogWithNoteData();
        } else mNote = new NoteModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBitmap != null) mBitmap.recycle();
    }

    @Override
    public void onClick(View view) {
        // if user clicked done button
        // gets user entered data and add it into note object
        // then send it to to presenter through main activity to handle it
        // else user clicked add image button
        // opens a media store broker to take image
        if (view.getId() == R.id.btn_done) {
            addNoteCheck();

        } else {
            startPictureIntent();
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
            if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                addNoteCheck();
                return true;
            }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // bottom drawable is at index 3 of edittext drawables
        final int DRAWABLE_BOTTOM = 3;
        // note image click listener
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (
                    editTextDescription.getBottom() - editTextDescription.getCompoundDrawables()[DRAWABLE_BOTTOM].getBounds().width())
                    && event.getRawX() <= editTextDescription.getBottom()) {
                imageClick(v);
                return true;
            }
        }
        return false;

    }


    //update the ui of the fragment to the opened note by user
    // transforming mBitmap into drawable to display it on edittext
    private void initDialogWithNoteData() {
        editTextTitle.setText(mNote.getText());
        editTextDescription.setText(mNote.getDescription());
        if (mNote.getImage() != null) {
            byte[] bytes = BitmapUtil.decodeImage(mNote.getImage());
            mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null,
                    null,
                    null,
                    new BitmapDrawable(getResources(), mBitmap));
            editTextDescription.setOnTouchListener(this);
        }
    }

    private void addNoteCheck() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        if (title.equals("") || description.equals("")) {
            Toast.makeText(mActivity, "check entries!", Toast.LENGTH_SHORT).show();
        } else {
            mNote.setText(title);
            mNote.setDescription(description);
            if (mUpdated != -1) // check if the note is newly added or an edited one
                mNote.setmDate(String.format("Edited on: %s",
                        new SimpleDateFormat("EEE, MMM d, ''yy hh:mm aaa",
                                Locale.getDefault()).format(new Date())));
            else
                mNote.setmDate(new SimpleDateFormat("EEE, MMM d, ''yy hh:mm aaa",
                        Locale.getDefault()).format(new Date()));

            // check if there is an image on edit text to add it into database
            if (editTextDescription.getCompoundDrawables()[3] != null)
                mNote.setImage(BitmapUtil.encodedImage(mBitmap));
            mSendNote.send(mNote, mUpdated);
            getDialog().dismiss();
        }
    }

    private void startPictureIntent() {
        try {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = BitmapUtil.createTempImageFile(mActivity);
            mFileLocation = BitmapUtil.mCurrentPhotoPath;
            Uri uri = FileProvider.getUriForFile(mActivity, "com.example.mg.todo", file);
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(i, REQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get user taken image and puts it into description edittext
    // then add to note object
    // compress  mBitmap to byte array using mBitmap CompressFormat
    // then encode it to base 64 to store it as a string into the database
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mBitmap = BitmapUtil.resamplePic(mFileLocation);
            editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null,
                    null,
                    null,
                    new BitmapDrawable(getResources(), mBitmap));
            editTextDescription.setOnTouchListener(this);
        }
    }

    private void imageClick(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.poupup_menu, popup.getMenu());

        //deletes image if user clicked delete button from menu
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete) {
                    editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null,
                            null,
                            null,
                            null);
                    mNote.setImage(null);
                    editTextDescription.setOnTouchListener(null);
                }
                return true;
            }
        });
        popup.show();
    }

    public interface ISendNoteObject {
        void send(NoteModel newNote, int mUpdated);
    }
}
