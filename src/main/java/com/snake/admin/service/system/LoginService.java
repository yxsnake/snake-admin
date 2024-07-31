package com.snake.admin.service.system;

import com.snake.admin.model.system.dto.LoginSysUserDTO;
import com.snake.admin.model.system.form.LoginSysUserForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginService {

    private final SysUserEntityService sysUserEntityService;

    private final SysUserRoleEntityService sysUserRoleEntityService;


    public LoginSysUserDTO login(LoginSysUserForm form) {

        return null;
    }
}
