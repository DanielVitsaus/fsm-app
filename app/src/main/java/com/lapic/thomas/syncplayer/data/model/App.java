package br.com.lapic.thomas.syncplayer.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by thomasmarquesbrandaoreis on 28/09/2017.
 */

public class App implements Parcelable {

    private String id;
    private ArrayList<Media> medias;

    public App() {}

    public App(String id, ArrayList<Media> medias) {
        this.id = id;
        this.medias = medias;
    }

    protected App(Parcel in) {
        id = in.readString();
        medias = in.createTypedArrayList(Media.CREATOR);
    }

    public static final Creator<App> CREATOR = new Creator<App>() {
        @Override
        public App createFromParcel(Parcel in) {
            return new App(in);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };

    public String getId() {
        return id;
    }

    public ArrayList<Media> getMedias() {
        return medias;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMedias(ArrayList<Media> medias) {
        this.medias = medias;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeTypedList(medias);
    }
}
