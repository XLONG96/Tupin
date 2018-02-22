package com.xlong.tupin.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="music")
public class Music implements Serializable{
    @Id
    @GeneratedValue
    private long id;

    private String cover;

    private String src;

    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
