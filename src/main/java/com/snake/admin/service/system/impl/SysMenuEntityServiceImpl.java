package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.snake.admin.mapper.system.SysMenuEntityMapper;
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
}
