package com.study.newbeeMall.dao;

import com.study.newbeeMall.entity.UserToken;

public interface UserTokenMapper{

    UserToken selectByToken(String token);

    UserToken selectByPrimaryKey(Long userId);

    int insertSelective(UserToken userToken);

    int updateByPrimaryKeySelective(UserToken userToken);

    int deleteByPrimaryKey(Long userId);
}




