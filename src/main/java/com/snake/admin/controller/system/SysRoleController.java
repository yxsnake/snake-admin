package com.snake.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snake.admin.model.system.dto.SysRoleDTO;
import com.snake.admin.model.system.equal.QuerySysRoleEqual;
import com.snake.admin.model.system.form.CreateSysRoleForm;
import com.snake.admin.model.system.form.ModifySysRoleForm;
import com.snake.admin.model.system.form.UpdateSysRoleStatusForm;
import com.snake.admin.model.system.fuzzy.QuerySysRoleFuzzy;
import com.snake.admin.service.system.SysRoleEntityService;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;
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

@Tag(name = "运营平台-角色")
@Slf4j
@RestController
@RequestMapping(value = "/sys-role",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private final SysRoleEntityService sysRoleEntityService;

    @Operation(summary = "创建角色")
    @PostMapping(value = "/create")
    public ResponseEntity<Result<Boolean>> create(@Validated @RequestBody CreateSysRoleForm form){
        sysRoleEntityService.create(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "修改角色")
    @PostMapping(value = "/modify")
    public ResponseEntity<Result<Boolean>> modify(@Validated @RequestBody ModifySysRoleForm form){
        sysRoleEntityService.modify(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "查询角色")
    @GetMapping(value = "/detail")
    public ResponseEntity<Result<SysRoleDTO>> detail(@RequestParam String id){
        return success(sysRoleEntityService.detail(id));
    }

    @Operation(summary = "分页查询角色列表")
    @PostMapping(value = "/page-list")
    public ResponseEntity<Result<IPage<SysRoleDTO>>> pageList(@RequestBody QueryFilter<QuerySysRoleEqual, QuerySysRoleFuzzy> queryFilter){
        return success(sysRoleEntityService.pageList(queryFilter));
    }

    @Operation(summary = "查询角色")
    @GetMapping(value = "/delete/{id}")
    public ResponseEntity<Result<Boolean>> deleteById(@PathVariable("id") String id){
        sysRoleEntityService.deleteById(id);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "更新角色状态")
    @PostMapping(value = "/update-status")
    public ResponseEntity<Result<Boolean>> updateStatus(@Validated @RequestBody UpdateSysRoleStatusForm form){
        sysRoleEntityService.updateStatus(form);
        return success(Boolean.TRUE);
    }
}
