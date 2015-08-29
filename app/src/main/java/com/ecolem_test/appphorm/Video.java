package com.ecolem_test.appphorm;

/**
 * Created by akawa_000 on 26/08/2015.
 */
public class Video {
    private String id;
    private String title;
    private String url;
    private boolean free;
    private boolean viewed;

    public Video(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
