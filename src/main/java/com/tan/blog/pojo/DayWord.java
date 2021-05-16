package com.tan.blog.pojo;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Author:TWH
 * @Date:2021/5/3 23:29
 */
public class DayWord implements Serializable {
    @Id
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String word;



    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
