package br.com.lapic.thomas.fsm_app.data.model;

import java.util.Map;

/**
 * Created by thomas on 21/08/17.
 */

public class Group {

    private Map<String, Media> medias;
    private Map<String, Anchor> anchors;

    public Group() {
    }

    public Group(Map<String, Media> medias, Map<String, Anchor> anchors) {
        this.medias = medias;
        this.anchors = anchors;
    }

    public Map<String, Media> getMedias() {
        return medias;
    }

    public Map<String, Anchor> getAnchors() {
        return anchors;
    }

    public void setMedias(Map<String, Media> medias) {
        this.medias = medias;
    }

    public void addMedia(Media media) {
        this.medias.put(media.getId(), media);
    }

    public void setAnchors(Map<String, Anchor> anchors) {
        this.anchors = anchors;
    }

    public void addAnchor(Anchor anchor) {
        this.anchors.put(anchor.getId(), anchor);
    }

}
