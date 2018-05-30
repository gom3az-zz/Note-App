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
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class NoteDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener, View.OnKeyListener {

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

    public NoteDialog() {
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
        // if user clicked on a note to update it it calls this function to update the ui of the fragment
        // else it init a new note object
        if (mNote != null) {
            initDialogWithNoteData();
        } else mNote = new NoteModel();
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
            startIntentToTakePicture();
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

    // get user taken image and puts it into description edittext
    // then add to note object
    // compress  bitmap to byte array using bitmap CompressFormat
    // then encode it to base 64 to store it as a string into the database
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapUtil.resamplePic(mFileLocation);
            editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null,
                    null,
                    null,
                    new BitmapDrawable(getResources(), bitmap));
            mNote.setImage(BitmapUtil.encodedImage(bitmap));
        }
    }

    //update the ui of the fragment to the opened note by user
    // transforming bitmap into drawable to display it on edittext
    private void initDialogWithNoteData() {
        editTextTitle.setText(mNote.getText());
        editTextDescription.setText(mNote.getDescription());
        if (mNote.getImage() != null) {
            byte[] bytes = BitmapUtil.decodeImage(mNote.getImage());
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null,
                    null,
                    null,
                    new BitmapDrawable(getResources(), bitmap));
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
            mSendNote.send(mNote, mUpdated);
            getDialog().dismiss();
        }
    }

    private void startIntentToTakePicture() {
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

    public void setModel(NoteModel model, int position) {
        this.mNote = model;
        mUpdated = position;
    }


    public interface ISendNoteObject {
        void send(NoteModel newNote, int mUpdated);
    }
}
