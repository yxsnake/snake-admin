package com.snake.admin.controller.system;

import com.snake.admin.service.system.SysMenuEntityService;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "运营平台-菜单")
@Slf4j
@RestController
@RequestMapping(value = "/sys-menu",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysMenuController extends BaseController {

    private final SysMenuEntityService sysMenuEntityService;
}
