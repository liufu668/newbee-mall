package com.study.newbeeMall.config.annotation;

import java.lang.annotation.*;

/**
 * 对查询用户数据的方法进行抽取,通过注解的形式返回用户信息,
 * 自定义了@TokenToUser注解
 */
@Target({ElementType.PARAMETER})//@TokenToUser注解只能用在方法上
@Retention(RetentionPolicy.RUNTIME)//@TokenToUser注解在运行时仍可用
@Documented//@TokenToUser注解应该被包含在Javadoc文档中
public @interface TokenToUser {

    /**
     * 当前用户在请求中的名字
     *
     * 这个属性 value 是一个字符串，具有默认值 "user"。你可以在使用 @TokenToUser 注解时指定这个值，或使用默认值 "user"
     */
    String value() default "user";
}
