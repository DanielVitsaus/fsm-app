package br.com.lapic.thomas.syncplayer.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
        if (this.medias == null)
            this.medias = new ArrayList<>();
        return medias;
    }

    public ArrayList<Anchor> getAnchors() {
        if (this.anchors == null)
            this.anchors = new ArrayList<>();
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

    public Media getMedia(int index) {
        return this.medias.get(index);
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

    public Media getMedia(String media) {
        for (Media media1 : medias) {
            if (media1.getId().equals(media))
                return media1;
        }
        return null;
    }
}
