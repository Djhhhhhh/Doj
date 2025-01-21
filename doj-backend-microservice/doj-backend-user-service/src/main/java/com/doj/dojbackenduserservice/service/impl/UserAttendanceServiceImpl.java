package com.doj.dojbackenduserservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doj.dojbackendmodel.entity.UserAttendance;
import com.doj.dojbackendmodel.vo.UserAttendanceVO;
import com.doj.dojbackenduserservice.mapper.UserAttendanceMapper;
import com.doj.dojbackenduserservice.service.UserAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserAttendanceServiceImpl extends ServiceImpl<UserAttendanceMapper, UserAttendance> implements UserAttendanceService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 用户签到
    @Override
    public String checkIn(long userId, HttpServletRequest request) {
        String redisKey = "attendance:" + userId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(redisKey)) {
            return "请勿重复签到";
        }

        // 查询数据库，检查用户是否已签到
        QueryWrapper<UserAttendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId).eq("status", true);
        UserAttendance userAttendance = this.getOne(queryWrapper);

        if (userAttendance != null) {
            return "请勿重复签到";
        }

        Date date = new Date();
        userAttendance = new UserAttendance();
        userAttendance.setUserId(userId);
        userAttendance.setCheckInTime(date);
        userAttendance.setDate(date);
        userAttendance.setStatus(true);
        boolean dbResult = this.save(userAttendance);

        if (dbResult) {
            ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
            zSetOperations.add("attendance:zset", userAttendance, date.getTime());

            valueOperations.set(redisKey, userAttendance);

            return "签到成功";
        } else {
            return "签到失败";
        }
    }

    // 用户签退
    @Override
    public String checkOut(long userId, HttpServletRequest request) {
        String redisKey = "attendance:" + userId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        UserAttendance userAttendance = (UserAttendance) valueOperations.get(redisKey);

        if (userAttendance == null) {
            return "未签到，无法签退";
        }
        redisTemplate.delete(redisKey);
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.remove("attendance:zset", userAttendance);
        userAttendance.setCheckOutTime(new Date());
        userAttendance.setStatus(false);
        boolean dbResult = this.updateById(userAttendance);
        if (dbResult) {
            return "签退成功";
        } else {
            return "签退失败";
        }
    }

    @Override
    public UserAttendanceVO getUserAttendanceStatus(long userId, HttpServletRequest request) {
        String redisKey = "attendance:" + userId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        UserAttendance userAttendance = (UserAttendance) valueOperations.get(redisKey);
        System.out.println("attendanceSet -----> " + userAttendance);

        if (userAttendance == null) {
            LambdaQueryWrapper<UserAttendance> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserAttendance::getUserId, userId)
                    .eq(UserAttendance::getStatus, true);
            userAttendance = this.getOne(queryWrapper);
        }

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
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Set<Object> attendanceSet = zSetOperations.range("attendance:zset", 0, -1);
        return attendanceSet.stream().map(attendanceObj -> {
            UserAttendance userAttendance = (UserAttendance) attendanceObj;
            UserAttendanceVO userAttendanceVO = new UserAttendanceVO();
            userAttendanceVO.setUserId(userAttendance.getUserId());
            userAttendanceVO.setCheckInTime(userAttendance.getCheckInTime());
            userAttendanceVO.setCheckInStatus(userAttendance.getStatus());
            return userAttendanceVO;
        }).collect(Collectors.toList());
    }
}