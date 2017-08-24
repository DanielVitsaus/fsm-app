package br.com.lapic.thomas.fsm_app.data.model;

import java.util.ArrayList;

/**
 * Created by thomas on 21/08/17.
 */

public class Media {

    private String id;
    private String src;
    private String type;
    private ArrayList<Group> groups;

    public Media() {
    }

    public Media(String id, String src, String type, ArrayList<Group> groups) {
        this.id = id;
        this.src = src;
        this.type = type;
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public String getSrc() {
        return src;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Group> getGroups() {
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

    public void addGroup(Group group) {
        if (groups == null)
            this.groups = new ArrayList<>();
        this.groups.add(group);
    }
}
