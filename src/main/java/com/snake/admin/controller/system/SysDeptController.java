package com.snake.admin.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snake.admin.common.Cons;
import com.snake.admin.model.system.dto.SysDeptDTO;
import com.snake.admin.model.system.form.CreateSysDeptForm;
import com.snake.admin.model.system.form.ModifySysDeptForm;
import com.snake.admin.service.system.SysDeptEntityService;
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
import java.util.stream.Collectors;

@Tag(name = "运营平台-部门")
@Slf4j
@RestController
@RequestMapping(value = "/sys-dept",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysDeptController extends BaseController {

    private final SysDeptEntityService sysDeptEntityService;

    @Operation(summary = "创建部门")
    @PostMapping(value = "/create",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:dept:create")
    public ResponseEntity<Result<Boolean>> create(@Validated @RequestBody CreateSysDeptForm form){
        sysDeptEntityService.create(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "修改部门")
    @PostMapping(value = "/modify",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:dept:modify")
    public ResponseEntity<Result<Boolean>> modify(@Validated @RequestBody ModifySysDeptForm form){
        sysDeptEntityService.modify(form);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "查询一个部门")
    @GetMapping(value = "/detail/{id}",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:dept:view")
    public ResponseEntity<Result<SysDeptDTO>> detail(@PathVariable("id") String id){
        return success(sysDeptEntityService.detail(id));
    }

    @Operation(summary = "删除一个部门")
    @GetMapping(value = "/delete/{id}",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:dept:delete")
    public ResponseEntity<Result<Boolean>> deleteById(@PathVariable("id") String id){
        sysDeptEntityService.deleteById(id);
        return success(Boolean.TRUE);
    }

    @Operation(summary = "查询部门列表")
    @GetMapping(value = "/list",headers = Cons.HEADER_AUTHORIZATION)
    @SaCheckPermission(value = "sys:dept:list")
    public ResponseEntity<Result<List<SysDeptDTO>>> list(){

        return success(sysDeptEntityService.list()
                .stream()
                .map(item->item.convert(SysDeptDTO.class))
                .collect(Collectors.toList())
        );
    }
}
