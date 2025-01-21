package com.doj.dojbackendmodel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 标签
 * @TableName tag
 */
@TableName(value ="tag")
@Data
public class Tag implements Serializable {
    
    /**
     * 标签ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
