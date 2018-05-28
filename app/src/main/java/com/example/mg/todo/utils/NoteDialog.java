package com.example.mg.todo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.DataModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.edit_text_title)
    EditText editTextTitle;
    @BindView(R.id.edit_text_description)
    EditText editTextDescription;
    @BindView(R.id.btn_done)
    Button btnDone;

    private ISendNoteObject lisISendNoteObject;

    public NoteDialog(Activity context) {
        super(context);
        try {
            lisISendNoteObject = (ISendNoteObject) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        ButterKnife.bind(this);
        btnDone.setOnClickListener(this);
        Objects.requireNonNull(this.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View view) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        if (title.equals("") || description.equals("")) {
            Toast.makeText(getContext(), "check entries!", Toast.LENGTH_SHORT).show();
        } else {
            DataModel newNote = new DataModel(title, description);
            lisISendNoteObject.send(newNote);
            this.hide();
        }

    }

    public interface ISendNoteObject {
        void send(DataModel newNote);
    }
}
