package com.doj.dojbackenduserservice.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.doj.dojbackendmodel.dto.user.UserQueryDTO;
import com.doj.dojbackendmodel.entity.User;
import com.doj.dojbackendmodel.vo.UserLoginVO;
import com.doj.dojbackendmodel.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author ASUS
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-08-22 00:53:19
*/
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    UserLoginVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    UserLoginVO getLoginUserVO(User user);

    User getLoginUser(HttpServletRequest request);
    User getLoginUserPermitNull(HttpServletRequest request);

    long userRegister(String userAccount, String userPassword, String checkPassword);

    boolean userLogout(HttpServletRequest request);

    UserVO getUserVO(User user);

    List<UserVO> getUserVO(List<User> userList);

    Wrapper<User> getQueryWrapper(UserQueryDTO userQueryRequest);

    boolean isAdmin(HttpServletRequest request);

    boolean isAdmin(User user);

}
