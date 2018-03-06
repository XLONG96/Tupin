package com.xlong.tupin.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="custom_ip")
public class CustomIP {
    @Id
    @GeneratedValue
    private long id;

    @Column(name="ip_addr")
    private String ipAddr;

    @Column(name="visit_num")
    private long visitNum;

    @Column(name="visit_time")
    private Date visitTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(long visitNum) {
        this.visitNum = visitNum;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }
}
