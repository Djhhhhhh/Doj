package com.doj.dojbackendmodel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 题目与标签关联
 * @TableName question_tag
 */
@TableName(value ="question_tag")
@Data
public class QuestionTag implements Serializable {

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 标签ID
     */
    private Long tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
