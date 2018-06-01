package com.example.mg.todo.ui.NoteFragment;

import android.content.Intent;
import android.view.View;

public interface INoteFragContract {
    interface IView {
    }

    interface IPresenter {

        void initFragmentData();

        void onDoneClick();

        void onTakeImageClick();

        void onActivityResult(int requestCode, Intent data);

        void onImageClick(View view);
    }
}

