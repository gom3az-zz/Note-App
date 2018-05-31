package com.example.mg.todo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteModel implements Parcelable {
    private String mText;
    private String mDescription;
    private String mImage;
    private String mDate;

    public static final Creator<NoteModel> CREATOR = new Creator<NoteModel>() {
        @Override
        public NoteModel createFromParcel(Parcel in) {
            return new NoteModel(in);
        }

        @Override
        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };

    public NoteModel() {

    }

    private NoteModel(Parcel in) {
        mText = in.readString();
        mDescription = in.readString();
        mImage = in.readString();
        mDate = in.readString();

    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mText);
        parcel.writeString(mDescription);
        parcel.writeString(mImage);
        parcel.writeString(mDate);
    }


}
