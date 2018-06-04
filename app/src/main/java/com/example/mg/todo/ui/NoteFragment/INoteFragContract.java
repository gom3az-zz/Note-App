package com.example.mg.todo.ui.NoteFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

public interface INoteFragContract {
    interface IView {
        void onImageAdded(Bitmap image, float factor);

        void onCancelImageCapture();

        void onFilledDataError();
    }

    interface IPresenter {

        void initFragmentData();

        void onDoneClick();

        void onTakeImageClick();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onImageClick(View view);


        void onViewImageClicked();

        void onDeleteImageClicked();
    }
}

