package com.snake.admin.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snake.admin.common.Cons;
import com.snake.admin.model.system.dto.SysMenuDTO;
import com.snake.admin.model.system.form.CreateSysMenuForm;
import com.snake.admin.model.system.form.ModifySysMenuForm;
import com.snake.admin.service.system.SysMenuEntityService;
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

@Tag(name = "运营平台-菜单")
@Slf4j
@RestController
@RequestMapping(value = "/sys-menu",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysMenuController extends BaseController {

    private final SysMenuEntityService sysMenuEntityService;

    @Operation(summary = "查询菜单列表")
    @GetMapping(value = "/list",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:menu:view")
    public ResponseEntity<Result<List<SysMenuDTO>>> list(){
        return success(sysMenuEntityService.queryList());
    }

    @Operation(summary = "创建菜单")
    @PostMapping(value = "/create",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:menu:create")
    public ResponseEntity<Result<Boolean>> create(@Validated @RequestBody CreateSysMenuForm form){
        sysMenuEntityService.create(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "修改菜单")
    @PostMapping(value = "/modify",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:menu:modify")
    public ResponseEntity<Result<Boolean>> modify(@Validated @RequestBody ModifySysMenuForm form){
        sysMenuEntityService.modify(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "删除一个菜单")
    @GetMapping(value = "/delete/{id}",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:menu:delete")
    public ResponseEntity<Result<Boolean>> deleteById(@PathVariable("id") String id){
        sysMenuEntityService.deleteById(id);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "查询一个菜单")
    @GetMapping(value = "/detail/{id}",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:menu:view")
    public ResponseEntity<Result<SysMenuDTO>> detail(@PathVariable("id") String id){
        return success(sysMenuEntityService.detail(id));
    }
}
