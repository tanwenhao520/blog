package com.tan.blog.requsetBean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tan.blog.pojo.Contents;
import lombok.Data;

@Data
public class FindPageRequest {
    @JsonProperty(value = "page",required = true)
    private Integer page;
    @JsonProperty(value = "row",required = true)
    private Integer row;
    @JsonProperty(value = "total",required = true)
    private Integer total;
    @JsonProperty(value = "contents",required = false)
    private Contents contents;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }
}
