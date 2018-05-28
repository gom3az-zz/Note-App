package com.example.mg.todo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.DataModel;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class NoteDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {

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
    private ISendNoteObject lisISendNoteObject;
    private Context activity;
    private DataModel newNote;
    String fileLocation;

    public NoteDialog() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = context;
        try {
            lisISendNoteObject = (ISendNoteObject) context;
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
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newNote = new DataModel();
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_done) {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            if (title.equals("") || description.equals("")) {
                Toast.makeText(activity, "check entries!", Toast.LENGTH_SHORT).show();
            } else {
                newNote.setText(title);
                newNote.setDescription(description);
                lisISendNoteObject.send(newNote);
                getDialog().dismiss();
            }
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = null;
            if (i.resolveActivity(activity.getPackageManager()) != null) {
                try {
                    file = BitmapUtil.createTempImageFile(activity);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file != null) {
                    fileLocation = BitmapUtil.mCurrentPhotoPath;
                    Uri uri = FileProvider.getUriForFile(activity, "com.example.mg.todo", file);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(i, REQUEST_CODE);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapUtil.resamplePic(fileLocation);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            editTextDescription.setCompoundDrawablesWithIntrinsicBounds(null, null, null, bitmapDrawable);
            newNote.setImage(BitmapUtil.encodedImage(bitmap));
        }
    }

    public interface ISendNoteObject {
        void send(DataModel newNote);
    }
}
