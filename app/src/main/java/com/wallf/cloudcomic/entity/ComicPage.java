package com.wallf.cloudcomic.entity;

import java.io.Serializable;

/**
 * Created by Fei.Liu on 2016/1/8.
 */
public class ComicPage implements Serializable {


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

}
