package br.com.lapic.thomas.fsm_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 21/08/17.
 */

public class Group implements Parcelable{

    private ArrayList<Media> medias;
    private ArrayList<Anchor> anchors;

    public Group() {
    }

    public Group(ArrayList<Media> medias, ArrayList<Anchor> anchors) {
        this.medias = medias;
        this.anchors = anchors;
    }

    protected Group(Parcel in) {
        medias = in.createTypedArrayList(Media.CREATOR);
        anchors = in.createTypedArrayList(Anchor.CREATOR);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public ArrayList<Media> getMedias() {
        return medias;
    }

    public ArrayList<Anchor> getAnchors() {
        return anchors;
    }

    public void setMedias(ArrayList<Media> medias) {
        this.medias = medias;
    }

    public void addMedia(Media media) {
        if (medias == null)
            this.medias = new ArrayList<>();
        this.medias.add(media);
    }

    public void setAnchors(ArrayList<Anchor> anchors) {
        this.anchors = anchors;
    }

    public void addAnchor(Anchor anchor) {
        if (anchors == null)
            this.anchors = new ArrayList<>();
        this.anchors.add(anchor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(medias);
        parcel.writeTypedList(anchors);
    }
}
