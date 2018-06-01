package com.example.mg.todo.ui.NoteFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class NoteFragment extends android.support.v4.app.DialogFragment
        implements INoteFragContract.IView,
        Button.OnClickListener,
        EditText.OnTouchListener {

    //private static final String TAG = "NoteFragment";
    public static final int REQUEST_CODE = 19;
    @BindView(R.id.edit_text_title)
    public
    EditText editTextTitle;
    @BindView(R.id.edit_text_description)
    public
    EditText editTextDescription;
    public ISendNoteObject mSendNote;
    public int mUpdated;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.btn_add_image)
    Button btnAddImage;
    Unbinder unbinder;
    private NoteFragmentPresenter mPresenter;
    private NoteModel mNote;


    public NoteFragment() {
    }

    public void setModel(NoteModel model, int position) {
        this.mNote = model;
        mUpdated = position;
    }

    @Override
    public void onAttach(Context context) {
        // casting sendNoteObject note object to parent activity to call its method
        super.onAttach(context);
        try {
            mSendNote = (ISendNoteObject) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement sendNoteObject data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_dialog, container, false);
        unbinder = ButterKnife.bind(this, v);
        btnDone.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        mPresenter = new NoteFragmentPresenter(this, mNote);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //update the ui of the fragment to the opened note by user
        // transforming mBitmap into drawable to display it on edittext
        mPresenter.initFragmentData();

    }

    @Override
    public void onClick(View view) {
        // if user clicked done button
        // gets user entered data and add it into note object
        // then sendNoteObject it to to presenter through main activity to handle it
        // else user clicked add image button
        // opens a media store broker to take image
        if (view.getId() == R.id.btn_done) {
            mPresenter.onDoneClick();

        } else {
            mPresenter.onTakeImageClick();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // bottom drawable is at index 3 of edittext drawables
        final int DRAWABLE_BOTTOM = 3;
        // note image click listener
        //Toast.makeText(mActivity, String.valueOf(event.getRawY()), Toast.LENGTH_SHORT).show();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (
                    editTextDescription.getBottom() -
                            editTextDescription.getCompoundDrawables()[DRAWABLE_BOTTOM].getBounds().width())
                    && event.getRawX() <= editTextDescription.getBottom()
                    && event.getRawY() >= editTextDescription.getBottom() -
                    editTextDescription.getCompoundDrawables()[DRAWABLE_BOTTOM].getBounds().width()) {
                mPresenter.onImageClick(v);
                return true;
            }
        }
        return false;

    }

    // get user taken image and puts it into description edittext
    // then add to note object
    // compress  mBitmap to byte array using mBitmap CompressFormat
    // then encode it to base 64 to store it as a string into the database
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.onActivityResult(requestCode, data);
        }
    }


    public interface ISendNoteObject {
        void sendNoteObject(NoteModel newNote, int mUpdated);
    }
}
