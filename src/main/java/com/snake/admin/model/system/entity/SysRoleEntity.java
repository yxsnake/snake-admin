package com.snake.admin.model.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import lombok.Data;

import java.util.Objects;

@Data
@TableName(value = "sys_role")
public class SysRoleEntity extends BaseEntity implements Convert {

    public final static String ROLE_CODE_ADMIN = "admin";

    @TableId(type = IdType.NONE)
    private String id;

    private String name;

    private String code;

    private Integer status;

    private String remark;

    @Override
    public String toString() {
        return JsonUtils.objectCovertToJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SysRoleEntity that = (SysRoleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(status, that.status) && Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, code, status, remark);
    }
}
