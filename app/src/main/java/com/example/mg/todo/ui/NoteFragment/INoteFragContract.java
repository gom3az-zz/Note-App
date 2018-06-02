package com.example.mg.todo.ui.NoteFragment;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

public interface INoteFragContract {
    interface IView {
        void onCancelImageCapture();

        void onFilledDataError();
    }

    interface IPresenter {

        void initFragmentData();

        void onDoneClick();

        void onTakeImageClick();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onImageClick(View view);

        void onTouch(View v, MotionEvent event);
    }
}

