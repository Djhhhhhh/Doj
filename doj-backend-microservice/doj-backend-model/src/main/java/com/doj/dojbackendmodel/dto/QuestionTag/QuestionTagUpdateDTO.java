package com.doj.dojbackendmodel.dto.QuestionTag;

import lombok.Data;

/**
 * @Author: _Djhhh
 * @Date: 2025/1/21 14:42
 * @Introduction：
 */
@Data
public class QuestionTagUpdateDTO {

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 标签ID
     */
    private Long OldTagId;

    private Long newTagId;
}
