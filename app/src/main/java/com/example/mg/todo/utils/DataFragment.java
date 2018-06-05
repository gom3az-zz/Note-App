package com.example.mg.todo.utils;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.mg.todo.data.model.NoteModel;


public class DataFragment extends Fragment {
    private NoteModel data;
    private String tempImage;
    private int mUpdated;

    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public NoteModel getData() {
        return data;
    }

    public void setData(NoteModel data) {
        this.data = data;
    }

    public String getTempImage() {
        return tempImage;
    }

    public void setTempImage(String tempImage) {
        this.tempImage = tempImage;
    }

    public int getmUpdated() {
        return mUpdated;
    }

    public void setmUpdated(int mUpdated) {
        this.mUpdated = mUpdated;
    }
}
