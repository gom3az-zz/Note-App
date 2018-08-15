package com.example.mg.todo.ui.NoteFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.mg.todo.App;
import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NoteFragment.DI.DaggerINoteFragmentComponent;
import com.example.mg.todo.ui.NoteFragment.DI.NoteFragmentModule;
import com.example.mg.todo.utils.BitmapUtil;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoteFragment extends DialogFragment
        implements INoteFragContract.IView,
        Button.OnClickListener {

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
    @BindView(R.id.image_note)
    ImageView imageNote;
    @BindView(R.id.btn_close_dialog)
    Button btnCloseDialog;

    //private static final String TAG = "NoteFragment";
    public static int mUpdated;

    @BindView(R.id.relative)
    LinearLayout relativeLayout;

    @Inject
    NoteFragmentPresenter mPresenter;

    @Inject
    RequestManager glide;

    private Unbinder unbinder;
    private NoteModel mNote;
    public ISendNoteObject mSendNote;


    public NoteFragment() {
    }

    public void setModel(NoteModel model, int position) {
        mNote = model;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_slide_animation);

        DaggerINoteFragmentComponent.builder()
                .iAppComponent(App.get(getActivity()).geAppComponent())
                .noteFragmentModule(new NoteFragmentModule(this))
                .build().inject(this);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_dialog, container, false);
        unbinder = ButterKnife.bind(this, v);
        btnDone.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
        btnCloseDialog.setOnClickListener(this);
        imageNote.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getDialog()
                .getWindow())
                .setWindowAnimations(R.style.dialog_slide_animation);
        if (savedInstanceState != null) {
            mPresenter.onRestoreState();
        }
        mPresenter.initFragmentData(mNote);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
        glide = null;
        mPresenter = null;
        this.dismiss();
    }

    @Override
    public void onClick(View view) {
        // if user clicked done button
        // gets user entered data and add it into note object
        // then sendNoteObject it to to presenter through main activity to handle it
        // else user clicked add image button
        // opens a media store broker to take image
        if (view.getId() == R.id.btn_done) mPresenter.onDoneClick();
        else if (view.getId() == R.id.image_note) mPresenter.onImageClick(view);
        else if (view.getId() == R.id.btn_close_dialog) this.dismiss();
        else mPresenter.onTakeImageClick();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageAdded(Bitmap mBitmap, float factor) {
        Bitmap bm = BitmapUtil.resize(mBitmap, factor);
        glide.asBitmap()
                .load(bm)
                .into(imageNote);
    }

    @Override
    public void onCancelImageCapture() {
        Toast.makeText(getContext(), "request canceled", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFilledDataError() {
        if (editTextDescription.getText().toString().equals(""))
            editTextDescription.setError("Missing!");
        if (editTextTitle.getText().toString().equals(""))
            editTextTitle.setError("Missing!");
    }

    public interface ISendNoteObject {
        void sendNoteObject(NoteModel newNote, int mUpdated);
    }
}
