package com.study.newbeeMall.dao;

import com.study.newbeeMall.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User user);
}
