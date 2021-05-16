package com.tan.blog.pojo;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class News implements Serializable {
    @Id
    private String id;
    private String title;
    private Date time;
    private String issuer;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
