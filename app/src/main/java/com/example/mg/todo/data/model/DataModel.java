package com.example.mg.todo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DataModel implements Parcelable {
    private String mText;
    private String mDescription;

    public DataModel(String text ) {
        this.mText = text;
    }

    protected DataModel(Parcel in) {
        mText = in.readString();
        mDescription = in.readString();
    }

    public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel in) {
            return new DataModel(in);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mText);
        parcel.writeString(mDescription);
    }
}
