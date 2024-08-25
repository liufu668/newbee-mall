package com.study.newbeeMall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 通过生成用户令牌(token)的形式进行用户状态的保持和验证.
 * 这里所说的token就是后端生成的一个字符串,
 * 该字符串与用户信息进行关联,token字符串通过一些无状态的数据生成,并不包含用户敏感信息
 */
@TableName(value ="user_token")
@Data
public class UserToken implements Serializable {
    /**
     * 用户主键id
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * token
     */
    private String token;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * token过期时间
     */
    private Date expireTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}