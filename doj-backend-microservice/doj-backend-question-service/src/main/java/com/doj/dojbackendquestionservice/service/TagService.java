package com.doj.dojbackendquestionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doj.dojbackendmodel.entity.Tag;

/**
 * @Author: _Djhhh
 * @Date: 2025/1/20 20:26
 * @Introduction：
 */
public interface TagService extends IService<Tag> {

    Tag add(String name);

    Tag getByName(String name);
}
