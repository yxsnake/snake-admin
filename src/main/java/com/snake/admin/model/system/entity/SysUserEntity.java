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

    public final static String DEFAULT_AVATAR = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fci.xiaohongshu.com%2Ff0100d63-59d2-87d2-ac5c-eef40c134026%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fci.xiaohongshu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1725387907&t=433cfd651e6521a8344a7239c08e7ae1";

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
