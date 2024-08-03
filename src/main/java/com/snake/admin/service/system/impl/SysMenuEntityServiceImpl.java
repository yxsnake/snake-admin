package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.snake.admin.common.enums.SysMenuTypeEnum;
import com.snake.admin.mapper.system.SysMenuEntityMapper;
import com.snake.admin.model.system.dto.RouteMenuDTO;
import com.snake.admin.model.system.dto.SysMenuDTO;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.form.CreateSysMenuForm;
import com.snake.admin.model.system.form.ModifySysMenuForm;
import com.snake.admin.service.system.SysMenuEntityService;
import com.snake.admin.service.system.SysRoleMenuEntityService;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuEntityServiceImpl extends ServiceImpl<SysMenuEntityMapper, SysMenuEntity> implements SysMenuEntityService {

    private final SysRoleMenuEntityService sysRoleMenuEntityService;
    @Override
    public List<SysMenuDTO> listByUserId(String userId) {
        List<SysMenuEntity> menus = this.getBaseMapper().selectMenuListByUserId(userId);
        if(CollUtil.isEmpty(menus)){
            return Lists.newArrayList();
        }
        return menus.stream().map(item->item.convert(SysMenuDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSysMenuForm form) {
        SysMenuTypeEnum sysMenuTypeEnum = SysMenuTypeEnum.getInstance(form.getMenuType());
        BizAssert.isTrue("请选择菜单类型",Objects.isNull(sysMenuTypeEnum));
        String parentId = form.getParentId();
        if(StrUtil.isBlank(parentId)){
            parentId = SysMenuEntity.ROOT_PARENT;
        }else{
            // 查询上级菜单是否存在
            SysMenuEntity sysMenuEntity = this.getBaseMapper().selectById(parentId);
            BizAssert.isTrue("上级菜单不存在",Objects.isNull(sysMenuEntity));
        }
        // 校验当前层级是否存在重复名称
        long count = this.lambdaQuery().eq(SysMenuEntity::getParentId, parentId).eq(SysMenuEntity::getTitle, form.getName()).list().stream().count();
        BizAssert.isTrue("菜单名称重复",count>0);
        if(SysMenuTypeEnum.MENU.equals(sysMenuTypeEnum)){
            form.checkMenuParam();
        }
        if(SysMenuTypeEnum.IFRAME.equals(sysMenuTypeEnum)){
            form.checkIFrameParam();
        }
        if(SysMenuTypeEnum.LINK.equals(sysMenuTypeEnum)){
            form.checkLinkParam();
        }
        if(SysMenuTypeEnum.BUTTON.equals(sysMenuTypeEnum)){
            form.checkButtonParam();
        }
        SysMenuEntity sysMenuEntity = form.convert(SysMenuEntity.class);
        String id = IdWorker.getIdStr();
        sysMenuEntity.setId(id);
        sysMenuEntity.setParentId(parentId);
        // TODO
        this.save(sysMenuEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifySysMenuForm form) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        // 查询菜单是否绑定角色
        Boolean existMenuBindRole = sysRoleMenuEntityService.existMenuBindRole(id);
        BizAssert.isTrue("当前菜单已分配给角色，无法删除",existMenuBindRole);
        this.getBaseMapper().deleteById(id);
    }

    @Override
    public SysMenuDTO detail(String id) {
        SysMenuEntity sysMenuEntity = this.getBaseMapper().selectById(id);
        if(Objects.isNull(sysMenuEntity)){
            return null;
        }
        return sysMenuEntity.convert(SysMenuDTO.class);
    }

    @Override
    public List<RouteMenuDTO> getCurrentUserRoutes(String userId) {

        return null;
    }
}
