package com.doj.dojbackendmodel.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户签到状态表
 */
@TableName("user_attendance")
@Data
public class UserAttendance  implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    /*
      编号
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 签到时间
     */
    private Date checkInTime;
    /**
     * 签退时间
     */
    private Date checkOutTime;
    /**
     * 签到日期
     */
    private Date date;
    /**
     * 状态（true签到/false签退）
     */
    private Boolean status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
