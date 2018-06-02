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
import android.widget.Toast;

import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoteFragment extends android.support.v4.app.DialogFragment
        implements INoteFragContract.IView,
        Button.OnClickListener,
        EditText.OnTouchListener {

    @BindView(R.id.edit_text_title)
    public
    EditText editTextTitle;
    @BindView(R.id.edit_text_description)
    public
    EditText editTextDescription;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.btn_add_image)
    Button btnAddImage;

    //private static final String TAG = "NoteFragment";
    private static final String KEY_UPDATED = "KEY_UPDATED";
    private static final String KEY_NOTE_MODEL = "KEY_NOTE_MODEL";
    private NoteFragmentPresenter mPresenter;
    private Unbinder unbinder;
    private NoteModel mNote;
    public ISendNoteObject mSendNote;
    public int mUpdated;

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

        if (savedInstanceState != null) {
            mUpdated = savedInstanceState.getInt(KEY_UPDATED);
            mNote = savedInstanceState.getParcelable(KEY_NOTE_MODEL);
        }
        //update the ui of the fragment to the opened note by user
        // transforming mBitmap into drawable to display it on edittext
        mPresenter.initFragmentData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_UPDATED, mUpdated);
        outState.putParcelable(KEY_NOTE_MODEL, mNote);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        // if user clicked done button
        // gets user entered data and add it into note object
        // then sendNoteObject it to to presenter through main activity to handle it
        // else user clicked add image button
        // opens a media store broker to take image
        if (view.getId() == R.id.btn_done) mPresenter.onDoneClick();
        else mPresenter.onTakeImageClick();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // bottom drawable is at index 3 of edittext drawables
        final int DRAWABLE_BOTTOM = 3;
        // note image click listener
        //Toast.makeText(mActivity, String.valueOf(event.getRawY()), Toast.LENGTH_SHORT).show();
        if (event.getAction() == MotionEvent.ACTION_UP)
            if (event.getRawX() >= (
                    editTextDescription.getBottom() -
                            editTextDescription.getCompoundDrawables()[DRAWABLE_BOTTOM].getBounds().width())
                    && event.getRawX() <= editTextDescription.getBottom()
                    && event.getRawY() >= editTextDescription.getBottom() -
                    editTextDescription.getCompoundDrawables()[DRAWABLE_BOTTOM].getBounds().width()) {
                mPresenter.onImageClick(v);
                return true;
            }
        return false;

    }

    // get user taken image and puts it into description edittext
    // then add to note object
    // compress  mBitmap to byte array using mBitmap CompressFormat
    // then encode it to base 64 to store it as a string into the database
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCancelImageCapture() {
        Toast.makeText(this.getContext(), "request canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFilledDataError() {
        Toast.makeText(this.getContext(), "check entries!", Toast.LENGTH_SHORT).show();

    }

    public interface ISendNoteObject {
        void sendNoteObject(NoteModel newNote, int mUpdated);
    }
}
