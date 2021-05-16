package com.tan.blog.service.impl;

import com.tan.blog.pojo.Tag;
import com.tan.blog.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TagServiceImpl extends BaseServiceImpl<Tag> implements TagService {
    public List<Tag> findAll() {
        return mapper.selectAll();
    }

    public int addTag(Tag tag){
        return mapper.insert(tag);
    }
}
