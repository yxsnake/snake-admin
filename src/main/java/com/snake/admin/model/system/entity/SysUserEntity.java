package com.snake.admin.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import lombok.Data;

@Data
@TableName(value = "sys_user")
public class SysUserEntity extends BaseEntity implements Convert {

    public final static String DEFAULT_AVATAR = "https://avatars.githubusercontent.com/u/4476132";

    public final static String SUPPER_ACCOUNT = "admin";

    @TableId(type = IdType.NONE)
    private String id;

    private String username;

    private String password;

    private String nickname;

    private String phone;

    private String email;

    private String avatar;

    private String remark;

    private Integer sex;

    private Integer status;

    private String deptId;

}
