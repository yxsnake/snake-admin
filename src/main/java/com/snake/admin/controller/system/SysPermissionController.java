package com.snake.admin.controller.system;

import com.snake.admin.common.Cons;
import com.snake.admin.model.system.dto.Route;
import com.snake.admin.service.system.impl.SysRouteService;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "运营平台-权限")
@Slf4j
@RestController
@RequestMapping(value = "/sys-permission",produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SysPermissionController extends BaseController {

    private final SysRouteService sysRouteService;

    @Operation(summary = "查询当前用户的菜单按钮权限列表树")
    @GetMapping(value = "/get/current-user/routes",headers = Cons.HEADER_AUTHORIZATION)
    public ResponseEntity<Result<List<Route>>> getCurrentUserRoutes(){
        return success(sysRouteService.currentUserRoutes());
    }
}
