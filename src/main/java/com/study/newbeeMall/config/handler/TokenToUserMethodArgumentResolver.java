package com.study.newbeeMall.config.handler;

import com.study.newbeeMall.NewbeeMallApplication;
import com.study.newbeeMall.common.Constants;
import com.study.newbeeMall.common.NewbeeMallException;
import com.study.newbeeMall.common.ServiceResultEnum;
import com.study.newbeeMall.config.annotation.TokenToUser;
import com.study.newbeeMall.dao.UserMapper;
import com.study.newbeeMall.dao.UserTokenMapper;
import com.study.newbeeMall.entity.User;
import com.study.newbeeMall.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自定义方法参数解析器
 * 通过方法参数解析器获得当前登录的对象信息
 */
@Component
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTokenMapper userTokenMapper;

    public TokenToUserMethodArgumentResolver(){

    }

    /**
     *
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if(parameter.hasParameterAnnotation(TokenToUser.class)){
            return true;
        }
        return false;
    }

    /**
     *
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if(parameter.getParameterAnnotation(TokenToUser.class) instanceof TokenToUser){
            User user = null;
            //获取请求头中的header
            String token = webRequest.getHeader("token");
            //验证token值是否存在
            //if(null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH){
            if(null != token && !"".equals(token)){
                //通过token值查询用户对象
                UserToken userToken = userTokenMapper.selectByToken(token);
                if(userToken == null || userToken.getExpireTime().getTime() <= System.currentTimeMillis()){
                    NewbeeMallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }
                user = userMapper.selectByPrimaryKey(userToken.getUserId());
                //用户不存在
                if(user == null){
                    NewbeeMallException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
                }
                if(user.getLockedFlag().intValue() == 1){
                    NewbeeMallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
                }
                //返回用户对象供对应的方法使用
                return user;
            }else{
                NewbeeMallException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }
}
