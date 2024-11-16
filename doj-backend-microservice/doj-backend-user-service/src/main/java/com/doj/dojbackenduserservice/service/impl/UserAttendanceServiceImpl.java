package com.doj.dojbackenduserservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doj.dojbackendmodel.entity.UserAttendance;
import com.doj.dojbackendmodel.vo.UserAttendanceVO;
import com.doj.dojbackenduserservice.mapper.UserAttendanceMapper;
import com.doj.dojbackenduserservice.service.UserAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserAttendanceServiceImpl extends ServiceImpl<UserAttendanceMapper, UserAttendance> implements UserAttendanceService {

    /**
     * 用户签到
     * @param userId 用户ID
     * @param request HTTP请求
     * @return 签到结果
     */
    @Override
    public String checkIn(long userId, HttpServletRequest request) {
        // 检查是否已经签到
        QueryWrapper<UserAttendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId)
                .eq("status", true);  // 已经签到

        // 查询用户是否已经签到
        UserAttendance userAttendance = this.getOne(queryWrapper);

        if (userAttendance != null) {
            return "请勿重复签到";
        }

        // 插入签到记录
        Date date=new Date();
        UserAttendance attendance = new UserAttendance();
        attendance.setUserId(userId);
        attendance.setCheckInTime(date);
        attendance.setDate(date);
        attendance.setStatus(true);

        return this.save(attendance) ? "签到成功" : "签到失败";
    }

    /**
     * 用户签退
     * @param userId 用户ID
     * @param request HTTP请求
     * @return 签退结果
     */
    @Override
    public String checkOut(long userId, HttpServletRequest request) {
        // 查找是否有签到记录
        QueryWrapper<UserAttendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId)
                .eq("status", true);  // 已经签到

        // 查询的签到记录
        UserAttendance userAttendance = this.getOne(queryWrapper);

        if (userAttendance == null) {
            return "未签到，无法签退";
        }

        userAttendance.setCheckOutTime(new Date());
        userAttendance.setStatus(false);

        return this.updateById(userAttendance) ? "签退成功" : "签退失败";
    }

    /**
     * 获取用户签到信息
     * @param userId 用户ID
     * @param request HTTP请求
     * @return 签到信息
     */
    @Override
    public UserAttendanceVO getUserAttendanceStatus(long userId, HttpServletRequest request) {
        LambdaQueryWrapper<UserAttendance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAttendance::getUserId, userId)
                .eq(UserAttendance::getStatus, true);

        UserAttendance userAttendance = this.getOne(queryWrapper);

        UserAttendanceVO userAttendanceVO = new UserAttendanceVO();
        userAttendanceVO.setUserId(userId);
        if (userAttendance != null) {
            userAttendanceVO.setCheckInStatus(userAttendance.getStatus());
            userAttendanceVO.setCheckInTime(userAttendance.getCheckInTime());
        } else {
            userAttendanceVO.setCheckInStatus(false);
        }
        return userAttendanceVO;
    }

    @Override
    public List<UserAttendanceVO> getUserAttendanceStatusList(HttpServletRequest request) {
        LambdaQueryWrapper<UserAttendance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAttendance::getStatus, true);
        List<UserAttendance> userAttendanceList =this.list(queryWrapper);
        return userAttendanceList.stream()
                .map(userAttendance -> {
                    UserAttendanceVO vo = new UserAttendanceVO();
                    vo.setUserId(userAttendance.getUserId());
                    vo.setCheckInTime(userAttendance.getCheckInTime());
                    vo.setCheckInStatus(userAttendance.getStatus());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
