package com.doj.dojbackendmodel.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserAttendanceVO {
    private long userId;
    private Date checkInTime;      // 签到时间
    private Boolean checkInStatus; // 签到状态
}
