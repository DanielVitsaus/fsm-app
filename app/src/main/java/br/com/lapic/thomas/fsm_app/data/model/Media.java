package br.com.lapic.thomas.fsm_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by thomas on 21/08/17.
 */

public class Media implements Parcelable {

    private String id;
    private String src;
    private String type;
    private int duration;
    private ArrayList<Group> groups;

    public Media() {
    }

    public Media(String id, String src, String type, ArrayList<Group> groups) {
        this.id = id;
        this.src = src;
        this.type = type;
        this.groups = groups;
    }

    protected Media(Parcel in) {
        id = in.readString();
        src = in.readString();
        type = in.readString();
        duration = in.readInt();
        groups = in.createTypedArrayList(Group.CREATOR);
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<Group> getGroups() {
        if (groups == null)
            this.groups = new ArrayList<>();
        return groups;
    }

    public Group getGroup(int index) {
        return groups.get(index);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void addGroup(Group group) {
        if (groups == null)
            this.groups = new ArrayList<>();
        this.groups.add(group);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(src);
        parcel.writeString(type);
        parcel.writeInt(duration);
        parcel.writeTypedList(groups);
    }
}
