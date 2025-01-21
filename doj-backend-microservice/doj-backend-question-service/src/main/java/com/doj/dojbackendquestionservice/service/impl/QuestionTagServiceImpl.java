package com.doj.dojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doj.dojbackendmodel.entity.QuestionTag;
import com.doj.dojbackendquestionservice.mapper.QuestionTagMapper;
import com.doj.dojbackendquestionservice.service.QuestionTagService;
import org.springframework.stereotype.Service;

/**
 * @Author: _Djhhh
 * @Date: 2025/1/21 14:28
 * @Introduction：
 */
@Service
public class QuestionTagServiceImpl extends ServiceImpl<QuestionTagMapper, QuestionTag> implements QuestionTagService {
}
