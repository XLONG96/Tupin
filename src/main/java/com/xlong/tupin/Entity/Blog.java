package com.xlong.tupin.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="blog")
public class Blog implements Serializable{
    @Id
    @GeneratedValue
    private long id;

    private String title;
    private String theme;

    @Column(name="visit_num")
    private long visitNum;

    @Column(name="public_time")
    private Date publicTime;

    private String summary;

    /**
     * The value of mdContent is markdown file's path at first
     */
    @Column(name="md_path")
    private String mdContent;

    @Transient
    private long lastId;
    @Transient
    private String lastBlogTitle;
    @Transient
    private long nextId;
    @Transient
    private String nextBlogTitle;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(long visitNum) {
        this.visitNum = visitNum;
    }

    public Date getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(Date publicTime) {
        this.publicTime = publicTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMdContent() {
        return mdContent;
    }

    public void setMdContent(String mdContent) {
        this.mdContent = mdContent;
    }

    public long getLastId() {
        return lastId;
    }

    public void setLastId(long lastId) {
        this.lastId = lastId;
    }

    public String getLastBlogTitle() {
        return lastBlogTitle;
    }

    public void setLastBlogTitle(String lastBlogTitle) {
        this.lastBlogTitle = lastBlogTitle;
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

    public String getNextBlogTitle() {
        return nextBlogTitle;
    }

    public void setNextBlogTitle(String nextBlogTitle) {
        this.nextBlogTitle = nextBlogTitle;
    }
}
