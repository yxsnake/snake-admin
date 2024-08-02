package com.snake.admin.controller.system;

import cn.dev33.satoken.stp.StpUtil;
import com.snake.admin.model.system.dto.LoginSysUserDTO;
import com.snake.admin.model.system.dto.RefreshTokenDTO;
import com.snake.admin.model.system.form.LoginSysUserForm;
import com.snake.admin.model.system.form.RefreshTokenForm;
import com.snake.admin.service.system.LoginService;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "运营平台-登录")
@Slf4j
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final LoginService loginService;

    @Operation(summary = "登录")
    @PostMapping(value = "/login")
    public ResponseEntity<Result<LoginSysUserDTO>> login(@Validated @RequestBody LoginSysUserForm form){
        return success(loginService.login(form));
    }

    @Operation(summary = "登出")
    @PostMapping(value = "/logout")
    public ResponseEntity<Result<Boolean>> logout(){
        StpUtil.logout(StpUtil.getLoginId());
        return success(Boolean.TRUE);
    }

    @Operation(summary = "刷新token")
    @PostMapping(value = "/refresh-token")
    public ResponseEntity<Result<RefreshTokenDTO>> refreshToken(@RequestBody RefreshTokenForm form){
        return success(loginService.refreshToken(form));
    }

}
