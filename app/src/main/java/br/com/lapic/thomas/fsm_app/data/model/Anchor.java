package br.com.lapic.thomas.fsm_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by thomas on 21/08/17.
 */

public class Anchor implements Parcelable {

    private String id;
    private int begin;
    private int end;
    private ArrayList<String> medias;

    public Anchor() {
    }

    public Anchor(String id, int begin, int end, ArrayList<String> medias) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.medias = medias;
    }

    protected Anchor(Parcel in) {
        id = in.readString();
        begin = in.readInt();
        end = in.readInt();
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

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
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

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
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
        parcel.writeInt(begin);
        parcel.writeInt(end);
        parcel.writeStringList(medias);
    }
}
