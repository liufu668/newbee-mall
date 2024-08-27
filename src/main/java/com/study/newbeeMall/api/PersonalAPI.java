package com.study.newbeeMall.api;

import com.study.newbeeMall.api.param.UserLoginParam;
import com.study.newbeeMall.api.param.UserUpdateParam;
import com.study.newbeeMall.api.vo.UserVO;
import com.study.newbeeMall.common.Constants;
import com.study.newbeeMall.config.annotation.TokenToUser;
import com.study.newbeeMall.entity.User;
import com.study.newbeeMall.service.UserService;
import com.study.newbeeMall.util.BeanUtil;
import com.study.newbeeMall.util.Result;
import com.study.newbeeMall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 处理登录请求
 */
@RestController
@Api(value = "v1", tags = "新蜂商城用户操作相关接口")
@RequestMapping("/api/v1")
public class PersonalAPI {

    @Resource
    private UserService userService;

    private final static Logger logger = LoggerFactory.getLogger(PersonalAPI.class);

    @PostMapping("/user/login")
    @ApiOperation(value = "登录接口", notes = "返回token")
    public Result<String> login(@RequestBody @Valid UserLoginParam userLoginParam){

        String loginResult = userService.login(userLoginParam.getLoginName(),userLoginParam.getPasswordMd5());
        logger.info("login api,loginName={},loginResult={}",userLoginParam.getLoginName(),loginResult);

        //登录成功
        //if(!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH){
        if(!StringUtils.isEmpty(loginResult)){
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }


    ///**
    // * 测试该接口,先把NewbeeMallException里的 throw new NewbeeMallException(message); 注释掉
    // * 否则接口里的代码无法执行
    // */
    //@GetMapping(value = "/test1")
    //@ApiOperation(value = "测试接口", notes = "方法中含有@TokenToUser注解")
    //public Result<String> test1(@TokenToUser User user){
    //    //此接口含有@TokenToUser注解,即需要登录验证的接口
    //    Result result = null;
    //    if(user == null){
    //        //如果通过请求header中的token未查询到用户即token无效,则登录验证失败,返回登录错误码
    //        result = ResultGenerator.genErrorResult(416, "未登录!");
    //    }else{
    //    //    登录验证通过
    //        result = ResultGenerator.genSuccessResult("登录验证通过!");
    //    }
    //    return result;
    //}
    //
    //@GetMapping(value = "/test2")
    //@ApiOperation(value = "测试接口", notes = "方法中无@TokenToUser注解")
    //public Result<String> test2(){
    //    //此接口不含@TokenToUser注解,即访问此接口无需登录验证
    //    Result result = ResultGenerator.genSuccessResult("此接口无需登录验证,请求成功!");
    //    //直接返回业务逻辑返回的数据即可
    //
    //    return result;
    //}


    @GetMapping("/user/info")
    @ApiOperation(value = "获取用户信息", notes = "")
    public Result<UserVO> getUserDetail(@TokenToUser User loginUser){
        //已登录则直接返回UserVO对象
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(loginUser, userVO);
        return ResultGenerator.genSuccessResult(userVO);
    }

    @PutMapping("/user/info")
    @ApiOperation(value = "修改用户信息", notes = "")
    public Result updateInfo(@RequestBody @ApiParam("用户信息") UserUpdateParam userUpdateParam, @TokenToUser User loginUser){

        Boolean flag = userService.updateUserInfo(userUpdateParam,loginUser.getUserId());
        if(flag){
            Result result = ResultGenerator.genSuccessResult();
            return result;
        }else{
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @PostMapping("/user/logout")
    @ApiOperation(value = "登出接口", notes = "清除token")
    public Result<String> logout(@TokenToUser User loginUser){
        //将当前已存在的token值设置为无效
        Boolean logoutResult = userService.logout(loginUser.getUserId());
        logger.info("logout api,loginUser={}", loginUser.getUserId());
        if(logoutResult){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("登出失败!");
    }
}