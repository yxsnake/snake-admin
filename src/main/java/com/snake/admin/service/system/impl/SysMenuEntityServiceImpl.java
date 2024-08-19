package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snake.admin.cache.system.SysMenuCacheService;
import com.snake.admin.common.enums.SysMenuTypeEnum;
import com.snake.admin.mapper.system.SysMenuEntityMapper;
import com.snake.admin.model.system.dto.SysMenuDTO;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.form.CreateSysMenuForm;
import com.snake.admin.model.system.form.ModifySysMenuForm;
import com.snake.admin.service.system.SysMenuEntityService;
import com.snake.admin.service.system.SysRoleMenuEntityService;
import com.snake.admin.service.system.SysUserRoleEntityService;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuEntityServiceImpl extends ServiceImpl<SysMenuEntityMapper, SysMenuEntity> implements SysMenuEntityService {

    private final SysRoleMenuEntityService sysRoleMenuEntityService;

    private final SysMenuCacheService sysMenuCacheService;

    private final SysUserRoleEntityService sysUserRoleEntityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSysMenuForm form) {
        SysMenuTypeEnum sysMenuTypeEnum = SysMenuTypeEnum.getInstance(form.getMenuType());
        SysMenuEntity menuForm = form.convert(SysMenuEntity.class);
        SysMenuEntity menuEntity = menuCommonCheck(menuForm, sysMenuTypeEnum);
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
        sysMenuEntity.setParentId(menuEntity.getParentId());
        this.save(sysMenuEntity);
        // 同时写入redis缓存
        sysMenuCacheService.writeMenuToCache(sysMenuEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifySysMenuForm form) {
        SysMenuTypeEnum sysMenuTypeEnum = SysMenuTypeEnum.getInstance(form.getMenuType());
        SysMenuEntity menuForm = form.convert(SysMenuEntity.class);
        SysMenuEntity menuEntity = menuCommonCheck(menuForm, sysMenuTypeEnum);
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
        this.update(menuEntity, Wrappers.lambdaUpdate(SysMenuEntity.class).eq(SysMenuEntity::getId,form.getId()));
        // 更新缓存
        SysMenuEntity dbMenuEntity = this.getBaseMapper().selectById(form.getId());
        sysMenuCacheService.writeMenuToCache(dbMenuEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        // 查询菜单是否绑定角色
        Boolean existMenuBindRole = sysRoleMenuEntityService.existMenuBindRole(id);
        BizAssert.isTrue("当前菜单已分配给角色，无法删除",existMenuBindRole);
        SysMenuEntity sysMenuEntity = this.getBaseMapper().selectById(id);
        if(Objects.nonNull(sysMenuEntity)){
            boolean contains = SysMenuEntity.NOT_ALLOW_DELETE_MENU_LIST.contains(sysMenuEntity.getPath());
            BizAssert.isTrue("当前菜单不允许删除",contains);
        }
        int deleteCount = this.getBaseMapper().deleteById(id);
        if(deleteCount > 0){
            // 从redis缓存中删除
            sysMenuCacheService.removeMenuCache(id);
        }
    }

    @Override
    public SysMenuDTO detail(String id) {
        // 优先读取缓存数据
        SysMenuEntity sysMenuEntity = sysMenuCacheService.readMenuFormCache(id);
        if(Objects.nonNull(sysMenuEntity)){
            return sysMenuEntity.convert(SysMenuDTO.class);
        }
        sysMenuEntity = this.getBaseMapper().selectById(id);
        if(Objects.isNull(sysMenuEntity)){
            return null;
        }
        return sysMenuEntity.convert(SysMenuDTO.class);
    }


    /**
     *
     * @param userId
     * @param containsAdminRole
     * @return
     */

    public List<SysMenuEntity> getMenuListByUserId(String userId,Boolean containsAdminRole){
        if(containsAdminRole){
            // 查询所有菜单
            List<SysMenuEntity> allMenuList = sysMenuCacheService.getAllMenuList();
            if(Objects.nonNull(allMenuList)){
                return allMenuList;
            }
            return this.list();
        }else{
            return this.getBaseMapper().selectMenuListByUserId(userId);
        }
    }

    @Override
    public List<SysMenuDTO> queryList() {
        return sysMenuCacheService.getAllMenuList()
                .stream()
                .map(sysMenuEntity -> sysMenuEntity.convert(SysMenuDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Set<String>> getMenuSubButtonPermsMap() {
        Map<String, Set<String>> menuSubButtonPermsMap = Maps.newHashMap();
        List<SysMenuEntity> allMenuList = sysMenuCacheService.getAllMenuList();
        List<SysMenuEntity> sysMenuEntities = allMenuList.stream().filter(menu -> SysMenuTypeEnum.MENU.getValue().equals(menu.getMenuType())).collect(Collectors.toList());
        sysMenuEntities.stream().forEach(menu->{
            menuSubButtonPermsMap.put(menu.getId(),Sets.newHashSet(menu.getAuths()));
        });

        for (Map.Entry<String, Set<String>> entry : menuSubButtonPermsMap.entrySet()) {
            String menuId = entry.getKey();
            Set<String> auths = entry.getValue();
            Set<String> perms = allMenuList.stream()
                    .filter(menu -> menuId.equals(menu.getParentId())).map(SysMenuEntity::getAuths)
                    .collect(Collectors.toSet());
            if(CollUtil.isNotEmpty(perms)){
                auths.addAll(perms);
                menuSubButtonPermsMap.put(menuId,auths);
            }

        }
        return menuSubButtonPermsMap;
    }

    @Override
    public List<SysMenuEntity> getCurrentMenuChildren(String menuId) {
        return this.lambdaQuery().eq(SysMenuEntity::getParentId,menuId).list();
    }

    @Override
    public Map<String, String> getPermissionMenuMap(String userId,Boolean adminRole) {
        Map<String, String> permissionMenuMap = Maps.newHashMap();
        Set<String> roleIds = null;
        // 查询当前用户所有角色
        if(!adminRole){
            roleIds = sysUserRoleEntityService.getRoleIdsByUserId(userId);
        }
        // 根据角色查询到所有菜单
        Set<String> menuIds = null;
        List<SysMenuEntity> sysMenuEntities = null;
        if(!adminRole){
            menuIds = sysRoleMenuEntityService.getMenuIdsByRoleIds(roleIds);
            if(CollUtil.isNotEmpty(menuIds)){
                sysMenuEntities = this.getBaseMapper().selectBatchIds(menuIds);
            }
        }else{
            // 查询所有菜单
            sysMenuEntities = this.list();
        }
        // 获取
        sysMenuEntities.stream().forEach(sysMenuEntity -> {
            if(StrUtil.isNotBlank(sysMenuEntity.getAuths())){
                permissionMenuMap.put(sysMenuEntity.getId(),sysMenuEntity.getAuths());
            }
        });
        return permissionMenuMap;
    }
    /////////////////////////////////////////////////以下为私有方法 为当前bean 内部使用 //////////////////////////////////////////////////


    private SysMenuEntity menuCommonCheck(SysMenuEntity form,SysMenuTypeEnum sysMenuTypeEnum){
        BizAssert.isTrue("请选择菜单类型",Objects.isNull(sysMenuTypeEnum));
        String parentId = form.getParentId();
        if(StrUtil.isBlank(parentId)){
            parentId = SysMenuEntity.ROOT_PARENT;
        }else{
            if(!SysMenuEntity.ROOT_PARENT.equals(parentId)){
                // 查询上级菜单是否存在
                SysMenuEntity sysMenuEntity = this.getBaseMapper().selectById(parentId);
                BizAssert.isTrue("上级菜单不存在",Objects.isNull(sysMenuEntity));
            }
        }
        // 校验当前层级是否存在重复名称
        long count = this.lambdaQuery()
                .eq(SysMenuEntity::getParentId, parentId)
                .eq(SysMenuEntity::getTitle, form.getName())
                .list().stream().count();
        BizAssert.isTrue("菜单名称重复",count>0);
        form.setParentId(parentId);
        return form;
    }
}
