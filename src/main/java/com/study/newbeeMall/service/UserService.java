package com.study.newbeeMall.service;

import com.study.newbeeMall.api.param.UserUpdateParam;

public interface UserService {
    String login(String loginName, String passwordMd5);

    Boolean updateUserInfo(UserUpdateParam userUpdateParam, Long userId);

    Boolean logout(Long userId);
}
