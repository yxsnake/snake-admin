package com.snake.admin.controller.system;

import com.snake.admin.model.system.form.AuthorizedSysRoleMenuForm;
import com.snake.admin.service.system.SysRoleMenuEntityService;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "运营平台-角色分类菜单")
@Slf4j
@RestController
@RequestMapping(value = "/sys-role-menu",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysRoleMenuController extends BaseController {

    private final SysRoleMenuEntityService sysRoleMenuEntityService;

    @Operation(summary = "角色授权菜单按钮")
    @PostMapping(value = "/authorized")
    public ResponseEntity<Result<Boolean>> authorizedMenu(AuthorizedSysRoleMenuForm form){
        sysRoleMenuEntityService.authorizedMenu(form);
        return success(Boolean.TRUE);
    }
}
