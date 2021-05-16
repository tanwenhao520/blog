package com.tan.blog.pojo;

import javax.persistence.Id;
import java.io.Serializable;

public class Tag implements Serializable {
    @Id
    private String id;
    private String tagName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
