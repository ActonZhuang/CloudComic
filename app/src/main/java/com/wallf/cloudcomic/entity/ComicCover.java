package com.wallf.cloudcomic.entity;

/**
 * Created by Fei.Liu on 2016/1/8.
 */
public class ComicCover {

    public ComicCover(String url, String title) {
        this.setUrl(url);
        this.setTitle(title);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

}