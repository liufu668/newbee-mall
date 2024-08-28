package com.study.newbeeMall.service.impl;

import com.study.newbeeMall.api.param.UserUpdateParam;
import com.study.newbeeMall.common.ServiceResultEnum;
import com.study.newbeeMall.dao.UserMapper;
import com.study.newbeeMall.dao.UserTokenMapper;
import com.study.newbeeMall.entity.User;
import com.study.newbeeMall.entity.UserToken;
import com.study.newbeeMall.service.UserService;
import com.study.newbeeMall.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.study.newbeeMall.util.NumberUtil;

import java.util.Date;

/**
 * 用户模块业务层代码的实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserTokenMapper userTokenMapper;

    @Override
    public String login(String loginName, String passwordMd5) {

        //根据登录名和密码查询用户
        User user = userMapper.selectByLoginNameAndPasswd(loginName,passwordMd5);
        if(user != null){
            if(user.getLockedFlag() == 1){//账号已锁定,无法登录
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }
            //登录后,执行修改token的操作,更新数据库里的token
            String token = getNewToken(System.currentTimeMillis() + "",user.getUserId());
             UserToken userToken = userTokenMapper.selectByPrimaryKey(user.getUserId());
            //当前时间
            Date now = new Date();
            //48小时后登录过期
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);
            if(userToken == null){//新增token
                userToken = new UserToken();
                userToken.setUserId(user.getUserId());
                userToken.setToken(token);
                userToken.setUpdateTime(now);
                userToken.setExpireTime(expireTime);
                if(userTokenMapper.insertSelective(userToken) > 0){//新增成功
                    return token;
                }
            }else{//更新token
                userToken.setToken(token);
                userToken.setUpdateTime(now);
                userToken.setExpireTime(expireTime);
                if(userTokenMapper.updateByPrimaryKeySelective(userToken) > 0){//修改成功
                    return token;
                }
            }
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public Boolean updateUserInfo(UserUpdateParam userUpdateParam, Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return false;
        }
        user.setNickName(userUpdateParam.getNickName());
        user.setPasswordMd5(userUpdateParam.getPasswordMd5());
        user.setIntroduceSign(userUpdateParam.getIntroduceSign());

        if(userMapper.updateByPrimaryKeySelective(user) > 0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean logout(Long userId) {
        return userTokenMapper.deleteByPrimaryKey(userId) > 0;
    }

    /**
     * 获取 token 值
     */
    private String getNewToken(String timeStr, Long userId){
        //拼接当前时间字符串、用户 ID 和一个四位随机数，生成一个源字符串
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }
}
