package com.snake.admin.controller.system;

import com.snake.admin.service.system.SysRoleEntityService;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "运营平台-角色")
@Slf4j
@RestController
@RequestMapping(value = "/sys-role",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private final SysRoleEntityService sysRoleEntityService;
}
