package com.snake.admin.service.system;

import com.snake.admin.common.enums.SysUserStatusEnum;
import com.snake.admin.model.system.dto.LoginSysUserDTO;
import com.snake.admin.model.system.entity.SysUserEntity;
import com.snake.admin.model.system.form.LoginSysUserForm;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginService {

    private final SysUserEntityService sysUserEntityService;

    private final SysUserRoleEntityService sysUserRoleEntityService;


    public LoginSysUserDTO login(LoginSysUserForm form) {
        SysUserEntity sysUserEntity = sysUserEntityService.lambdaQuery()
                .eq(SysUserEntity::getUsername, form.getAccount())
                .list().stream().findFirst().orElse(null);
        BizAssert.isTrue("用户不存在", Objects.isNull(sysUserEntity));
        SysUserStatusEnum sysUserStatusEnum = SysUserStatusEnum.getInstance(sysUserEntity.getStatus());
        BizAssert.isTrue("账号已禁用，请联系系统管理员",SysUserStatusEnum.DISABLE.equals(sysUserStatusEnum));

        return null;
    }
}
