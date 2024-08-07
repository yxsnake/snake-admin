package com.snake.admin.controller.system;

import com.snake.admin.common.Cons;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "运营平台-角色分类菜单")
@Slf4j
@RestController
@RequestMapping(value = "/sys-role-menu",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysRoleMenuController extends BaseController {

    private final SysRoleMenuEntityService sysRoleMenuEntityService;

    @Operation(summary = "角色授权菜单按钮")
    @PostMapping(value = "/authorized",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<Boolean>> authorizedMenu(@Validated @RequestBody AuthorizedSysRoleMenuForm form){
        sysRoleMenuEntityService.authorizedMenu(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "根据角色查询菜单ID")
    @GetMapping(value = "/role-menu-ids/{roleId}",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<List<String>>> getMenuIdsByRoleId(@PathVariable("roleId") String roleId){
        return success(sysRoleMenuEntityService.getMenuIdsByRoleId(roleId));
    }
}
