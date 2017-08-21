package br.com.lapic.thomas.fsm_app.data.model;

import java.util.ArrayList;

/**
 * Created by thomas on 21/08/17.
 */

public class Anchor {

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
        this.medias.add(mediaId);
    }

}
