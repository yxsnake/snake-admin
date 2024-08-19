package com.snake.admin.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snake.admin.common.Cons;
import com.snake.admin.model.system.form.AuthorizedSysUserRoleForm;
import com.snake.admin.service.system.SysUserRoleEntityService;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "运营平台-用户分配角色")
@Slf4j
@RestController
@RequestMapping(value = "/sys-use-role",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysUserRoleController extends BaseController {

    private final SysUserRoleEntityService sysUserRoleEntityService;

    @Operation(summary = "用户授权角色")
    @PostMapping(value = "/authorized",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:user:auth-role")
    public ResponseEntity<Result<Boolean>> authorizedRole(@Validated @RequestBody AuthorizedSysUserRoleForm form){
        sysUserRoleEntityService.authorizedRole(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "获取当前用户的所有角色ID")
    @GetMapping(value = "/list-role-ids/{userId}")
    public ResponseEntity<Result<Set<String>>> getRoleIdsByUserId(@PathVariable("userId") String userId){
        return success(sysUserRoleEntityService.getRoleIdsByUserId(userId));
    }


}
