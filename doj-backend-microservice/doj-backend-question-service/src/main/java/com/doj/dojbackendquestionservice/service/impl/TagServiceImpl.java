package com.doj.dojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doj.dojbackendmodel.entity.Tag;
import com.doj.dojbackendquestionservice.mapper.TagMapper;
import com.doj.dojbackendquestionservice.service.TagService;
import org.springframework.stereotype.Service;

/**
 * @Author: _Djhhh
 * @Date: 2025/1/20 20:40
 * @Introduction：
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    public Tag add(String name) {
        synchronized (name.intern()){
            long count = this.baseMapper.selectCount(new QueryWrapper<Tag>().eq("name", name));
            if(count>0){
                return null;
            }
            Tag tag = new Tag();
            tag.setName(name);
            if(this.save(tag)){
                return tag;
            }else{
                return null;
            }
        }
    }

    @Override
    public Tag getByName(String name) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        Tag tag = this.baseMapper.selectOne(tagQueryWrapper.eq("name",name));
        return tag;
    }
}
