package com.snake.admin.service.system;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.snake.admin.common.components.RefreshTokenService;
import com.snake.admin.common.enums.SysUserStatusEnum;
import com.snake.admin.common.security.sm3.SM3Util;
import com.snake.admin.common.utils.PwdUtil;
import com.snake.admin.model.system.dto.LoginSysUserDTO;
import com.snake.admin.model.system.entity.SysUserEntity;
import com.snake.admin.model.system.form.LoginSysUserForm;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginService {

    private final SysUserEntityService sysUserEntityService;

    private final SysUserRoleEntityService sysUserRoleEntityService;

    private final RefreshTokenService refreshTokenService;


    public LoginSysUserDTO login(LoginSysUserForm form) {

        LoginSysUserDTO loginSysUser = new LoginSysUserDTO();


        SysUserEntity sysUserEntity = sysUserEntityService.lambdaQuery()
                .eq(SysUserEntity::getUsername, form.getAccount())
                .list().stream().findFirst().orElse(null);
        BizAssert.isTrue("用户不存在", Objects.isNull(sysUserEntity));
        SysUserStatusEnum sysUserStatusEnum = SysUserStatusEnum.getInstance(sysUserEntity.getStatus());
        BizAssert.isTrue("账号已禁用，请联系系统管理员",SysUserStatusEnum.DISABLE.equals(sysUserStatusEnum));
        String username = sysUserEntity.getUsername();
        String rawPassword = form.getPassword();
        Boolean verify = PwdUtil.verify(username, rawPassword, sysUserEntity.getPassword());
        BizAssert.isTrue("密码错误",!verify);

        String userId = sysUserEntity.getId();
        StpUtil.logout(userId);
        StpUtil.login(userId);
        String token = StpUtil.getTokenValue();
        loginSysUser.setUserId(userId);
        loginSysUser.setAccessToken(token);

        if(StrUtil.isBlank(sysUserEntity.getAvatar())){
            loginSysUser.setAvatar(SysUserEntity.DEFAULT_AVATAR);
        }

        // 生成刷新token
        String refreshToken = RefreshTokenService.generateRefreshToken();
        refreshTokenService.setRefreshToken(refreshToken,userId);

        long tokenTimeout = StpUtil.getTokenTimeout() * 1000;
        long expiresNumber = DateUtil.date().getTime() + tokenTimeout;
        log.info("超时时间：{}",expiresNumber);
        loginSysUser.setExpires(new Date(expiresNumber));

        loginSysUser.setRefreshToken(refreshToken);

        // 查询当前用的角色标识
        Set<String> roleCodes =  sysUserRoleEntityService.getCurrentUserRoles(userId);
        loginSysUser.setRoles(roleCodes);

        return loginSysUser;
    }
}
