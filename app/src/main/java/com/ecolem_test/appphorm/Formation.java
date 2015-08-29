package com.ecolem_test.appphorm;

/**
 * Created by akawa_000 on 15/08/2015.
 */
public class Formation {
    private String id;
    private String title;
    private String description;
    private boolean done;

    public Formation(String id, String title, String description) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.done = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
