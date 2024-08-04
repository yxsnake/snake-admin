package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snake.admin.common.enums.SysMenuTypeEnum;
import com.snake.admin.mapper.system.SysMenuEntityMapper;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.model.system.dto.RouteMenuDTO;
import com.snake.admin.model.system.dto.RouteMenuMetaDTO;
import com.snake.admin.model.system.dto.SysMenuDTO;
import com.snake.admin.model.system.entity.SysMenuEntity;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.entity.SysRoleMenuEntity;
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

    private final SysUserRoleEntityService sysUserRoleEntityService;

    private final SysRoleEntityMapper sysRoleEntityMapper;

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
        List<RouteMenuDTO> routeMenuList = Lists.newArrayList();
        // 查询 用户的角色标识 是否包含管理员标识
        Boolean containsAdminRole =  sysUserRoleEntityService.containsAdminRole(userId);
        List<SysMenuEntity> list = this.getCurrentMenuIds(userId,containsAdminRole);
        if(CollUtil.isEmpty(list)){
            return routeMenuList;
        }else{
            // 查询菜单角色关系
            Set<String> menuIds = list.stream()
                    .filter(sysMenuEntity -> !SysMenuTypeEnum.BUTTON.equals(sysMenuEntity.getMenuType()))
                    .map(SysMenuEntity::getId).collect(Collectors.toSet());
            Map<String, Set<String>> menuAllowRolesMap = getMenuAllowRoles(menuIds);

            Set<String> buttonIds = list.stream()
                    .filter(sysMenuEntity -> SysMenuTypeEnum.BUTTON.equals(sysMenuEntity.getMenuType()))
                    .map(SysMenuEntity::getId).collect(Collectors.toSet());

            Map<String, Set<String>> buttonAllowAuthMap = getButtonAllowAuth(buttonIds);


            list.stream().forEach(menu->{
                String menuId = menu.getId();
                Integer menuType = menu.getMenuType();
                SysMenuTypeEnum menuTypeEnum = SysMenuTypeEnum.getInstance(menuType);
                RouteMenuDTO routeMenuDTO = new RouteMenuDTO();
                routeMenuDTO.setId(menuId);
                routeMenuDTO.setMenuType(menuType);
                routeMenuDTO.setName(menu.getName());
                routeMenuDTO.setPath(menu.getPath());
                routeMenuDTO.setParentId(menu.getParentId());
                routeMenuDTO.setComponent(menu.getComponent());

                RouteMenuMetaDTO menuMetaDTO = new RouteMenuMetaDTO();
                menuMetaDTO.setIcon(menu.getIcon());
                menuMetaDTO.setTitle(menu.getTitle());
                if(SysMenuTypeEnum.DIR.equals(menuTypeEnum)){
                    menuMetaDTO.setRank(menu.getRank());
                }
                SysMenuTypeEnum sysMenuTypeEnum = SysMenuTypeEnum.getInstance(menuType);
                if(SysMenuTypeEnum.BUTTON.equals(sysMenuTypeEnum)){
                    Set<String> auths = buttonAllowAuthMap.get(menuId);
                    // 如果是系统管理员角色
                    if(containsAdminRole){
                        auths = list.stream()
                                .filter(sysMenuEntity -> SysMenuTypeEnum.BUTTON.equals(sysMenuEntity.getMenuType()))
                                .map(SysMenuEntity::getAuths).collect(Collectors.toSet());

                    }
                    menuMetaDTO.setAuths(auths);
                }else if(SysMenuTypeEnum.MENU.equals(sysMenuTypeEnum) || SysMenuTypeEnum.LINK.equals(sysMenuTypeEnum)){
                    Set<String> roles = menuAllowRolesMap.get(menuId);
                    // 如果是系统管理员角色
                    if(containsAdminRole){
                        roles = Sets.newHashSet();
                        roles.add(SysRoleEntity.ROLE_CODE_ADMIN);
                    }
                    menuMetaDTO.setRoles(roles);

                }
                routeMenuDTO.setMeta(menuMetaDTO);
                routeMenuList.add(routeMenuDTO);
            });
        }

        List<RouteMenuDTO> tree = streamToTree(routeMenuList, SysMenuEntity.ROOT_PARENT);
        return tree;
    }

    /**
     * 查询菜单允许访问的角色标识
     * key 为菜单， value 为当前菜单可访问的角色标识
     * @param menuIds
     * @return
     */
    private Map<String,Set<String>> getMenuAllowRoles(Set<String> menuIds){
        Map<String,Set<String>> menuRoleCodeListMap = Maps.newHashMap();
        List<SysRoleMenuEntity> roleMenuEntities = sysRoleMenuEntityService.lambdaQuery().in(SysRoleMenuEntity::getMenuId, menuIds).list();
        if(CollUtil.isEmpty(roleMenuEntities)){
            return menuRoleCodeListMap;
        }
        // 菜单与角色映射关系
        Map<String, List<SysRoleMenuEntity>> menuRoleMap = roleMenuEntities.stream().collect(Collectors.groupingBy(SysRoleMenuEntity::getMenuId));
        Set<String> roleIds = roleMenuEntities.stream().map(SysRoleMenuEntity::getRoleId).collect(Collectors.toSet());
        List<SysRoleEntity> sysRoleEntities = sysRoleEntityMapper.selectBatchIds(roleIds);
        if(CollUtil.isEmpty(sysRoleEntities)){
            return menuRoleCodeListMap;
        }
        // 角色 ID 与 角色标识 映射关系
        Map<String, String> roleMap = sysRoleEntities.stream().collect(Collectors.toMap(SysRoleEntity::getId, row -> row.getCode()));
        menuIds.stream().forEach(menuId->{
            List<SysRoleMenuEntity> sysRoleMenuEntities = menuRoleMap.get(menuId);
            Set<String> roles = Sets.newHashSet();
            sysRoleMenuEntities.stream().forEach(sysRoleMenuEntity -> {
                String roleId = sysRoleMenuEntity.getRoleId();
                String roleCode = roleMap.get(roleId);
                if(StrUtil.isNotBlank(roleCode)){
                    roles.add(roleCode);
                }
            });
            menuRoleCodeListMap.put(menuId,roles);
        });
        return menuRoleCodeListMap;
    }

    private Map<String,Set<String>> getButtonAllowAuth(Set<String> buttonIds){
        Map<String,Set<String>> buttonAllowAuthMap = Maps.newHashMap();
        if(CollUtil.isEmpty(buttonIds)){
            return buttonAllowAuthMap;
        }
        List<SysMenuEntity> sysMenuEntities = this.getBaseMapper().selectBatchIds(buttonIds);
        sysMenuEntities.stream().forEach(sysMenuEntity -> {
            String buttonId = sysMenuEntity.getId();
            Set<String> auths = buttonAllowAuthMap.get(buttonId);

            if(Objects.isNull(auths)){
                auths = Sets.newHashSet();
            }
            auths.add(sysMenuEntity.getAuths());
            buttonAllowAuthMap.put(buttonId,auths);
        });
        return buttonAllowAuthMap;
    }

    public List<SysMenuEntity> getCurrentMenuIds(String userId,Boolean containsAdminRole){
        if(containsAdminRole){
            // 查询所有菜单
            return this.lambdaQuery().list();
        }else{
            return this.getBaseMapper().selectMenuListByUserId(userId);
        }
    }

    private List<RouteMenuDTO> streamToTree(List<RouteMenuDTO> routes, String parentId) {
        List<RouteMenuDTO> list = routes.stream()
                .filter(item -> item.getParentId().equals(parentId))
                .collect(Collectors.toList());

        list = list.stream().map(item->{
            item.setChildren(streamToTree(routes,item.getId()));
            return item;
        }).collect(Collectors.toList());
        return list;
    }



}
