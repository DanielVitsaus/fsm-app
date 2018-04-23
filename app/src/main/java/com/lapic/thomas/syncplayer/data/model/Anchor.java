package com.lapic.thomas.syncplayer.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;

import com.lapic.thomas.syncplayer.helper.StringHelper;

/**
 * Created by thomas on 21/08/17.
 */

public class Anchor implements Parcelable, Comparator<Anchor> {

    private String id;
    private String begin;
    private String end;
    private ArrayList<String> medias;

    public Anchor() {
    }

    protected Anchor(Parcel in) {
        id = in.readString();
        begin = in.readString();
        end = in.readString();
        medias = in.createStringArrayList();
    }

    public static final Creator<Anchor> CREATOR = new Creator<Anchor>() {
        @Override
        public Anchor createFromParcel(Parcel in) {
            return new Anchor(in);
        }

        @Override
        public Anchor[] newArray(int size) {
            return new Anchor[size];
        }
    };

    public String getId() {
        return id;
    }

    public int getBeginInt() {
        return StringHelper.removeLettersAndParseInt(begin);
    }

    public String getBegin() {
        return begin;
    }

    public int getEndInt() {
        return StringHelper.removeLettersAndParseInt(end);
    }

    public String getEnd() {
        return end;
    }

    public ArrayList<String> getMedias() {
        return medias;
    }

    public String getMedia(int index) {
        return medias.get(index);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBeginInt(int begin) {
        this.begin = begin + "s";
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEndInt(int end) {
        this.end = end + "s";
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setMedias(ArrayList<String> medias) {
        this.medias = medias;
    }

    public void addMedia(String mediaId) {
        if (medias == null)
            this.medias = new ArrayList<>();
        this.medias.add(mediaId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(begin);
        parcel.writeString(end);
        parcel.writeStringList(medias);
    }


    @Override
    public int compare(Anchor anchor1, Anchor anchor2) {
        return StringHelper.removeLettersAndParseInt(anchor1.getBegin()) < StringHelper.removeLettersAndParseInt(anchor2.getBegin()) ? 1
                : (anchor1.getBegin() == anchor2.getBegin() ? 0 : -1);
    }

}
