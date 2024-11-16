package com.doj.dojbackenduserservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doj.dojbackendmodel.entity.UserAttendance;
import com.doj.dojbackendmodel.vo.UserAttendanceVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserAttendanceService extends IService<UserAttendance> {
    String checkIn(long userId, HttpServletRequest request);

    String checkOut(long userId, HttpServletRequest request);

    UserAttendanceVO getUserAttendanceStatus(long userId, HttpServletRequest request);

    List<UserAttendanceVO> getUserAttendanceStatusList(HttpServletRequest request);
}
