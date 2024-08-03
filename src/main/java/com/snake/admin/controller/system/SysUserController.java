package com.snake.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snake.admin.common.Cons;
import com.snake.admin.model.system.dto.SysUserDTO;
import com.snake.admin.model.system.equal.QuerySysUserEqual;
import com.snake.admin.model.system.form.CreateSysUserForm;
import com.snake.admin.model.system.form.ModifySysUserForm;
import com.snake.admin.model.system.form.RestUsrPwdForm;
import com.snake.admin.model.system.form.UpdateSysUserStatusForm;
import com.snake.admin.model.system.fuzzy.QuerySysUserFuzzy;
import com.snake.admin.service.system.SysUserEntityService;
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

@Tag(name = "运营平台-用户")
@Slf4j
@RestController
@RequestMapping(value = "/sys-user",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysUserController extends BaseController {

    private final SysUserEntityService sysUserEntityService;

    @Operation(summary = "创建用户")
    @PostMapping(value = "/create",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<Boolean>> create(@Validated @RequestBody CreateSysUserForm form){
        sysUserEntityService.create(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "修改用户")
    @PostMapping(value = "/modify",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<Boolean>> modify(@Validated @RequestBody ModifySysUserForm form){
        sysUserEntityService.modify(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "查询用户信息")
    @GetMapping(value = "/detail/{id}",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<SysUserDTO>> detail(@PathVariable("id") String id){
        return success(sysUserEntityService.detail(id));
    }

    @Operation(summary = "修改用户状态")
    @PostMapping(value = "/update-status",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<Boolean>> updateStatus(@Validated @RequestBody UpdateSysUserStatusForm form){
        sysUserEntityService.upateStatus(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "分页查询用户列表")
    @PostMapping(value = "/page-list",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<IPage<SysUserDTO>>> pageList(@RequestBody QueryFilter<QuerySysUserEqual, QuerySysUserFuzzy> queryFilter){
        return success(sysUserEntityService.pageList(queryFilter));
    }

    @Operation(summary = "重置用户密码")
    @PostMapping(value = "/rest-password",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<Boolean>> restPassword(@Validated @RequestBody RestUsrPwdForm form){
        sysUserEntityService.restPassword(form);
        return success(Boolean.TRUE);
    }

}
