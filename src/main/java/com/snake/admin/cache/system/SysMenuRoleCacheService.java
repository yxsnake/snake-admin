package com.snake.admin.cache.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snake.admin.common.Cons;
import com.snake.admin.mapper.system.SysRoleMenuEntityMapper;
import com.snake.admin.model.system.entity.SysRoleMenuEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SysMenuRoleCacheService {


    private final static String MENU_ROLE_ID_LIST = "menu:role_id:list";
    private final static String MENU_ROLE_CACHE_HASH_KEY = String.format(Cons.CACHE_REDIS_HASH_FORMAT,Cons.CACHE_PREFIX,MENU_ROLE_ID_LIST);

    private final SysRoleMenuEntityMapper sysRoleMenuEntityMapper;

    private final RedisTemplate redisTemplate;

    public void init(){
        log.info("=============================> 开始初始化菜单与角色关系 <============================================");
        Map<String, Set<String>> menuRoleIdsMap = queryMenuRoleIdsMap();
        redisTemplate.opsForHash().entries(MENU_ROLE_CACHE_HASH_KEY).forEach((k,v)->{
            Set<String> roleIds = Sets.newHashSet(String.valueOf(v).split(StringPool.COMMA));
            removeMenuRoleIds(String.valueOf(k),roleIds);
        });
        for (Map.Entry<String, Set<String>> entry : menuRoleIdsMap.entrySet()) {
            writeMenuRoleIdsCache(entry.getKey(),entry.getValue());
        }
        log.info("=============================> 结束初始化菜单与角色关系 <============================================");
    }


    private Map<String, Set<String>> queryMenuRoleIdsMap(){
        Map<String, Set<String>> menuRoleIdsMap = Maps.newHashMap();
        List<SysRoleMenuEntity> roleMenuEntities = sysRoleMenuEntityMapper.selectList(Wrappers.lambdaQuery(SysRoleMenuEntity.class));
        if(CollUtil.isNotEmpty(roleMenuEntities)){
            roleMenuEntities.stream().forEach(sysRoleMenuEntity -> {
                Set<String> roleIds = menuRoleIdsMap.get(sysRoleMenuEntity.getMenuId());
                if(Objects.isNull(roleIds)){
                    roleIds = Sets.newHashSet();
                }
                roleIds.add(sysRoleMenuEntity.getRoleId());
                menuRoleIdsMap.put(sysRoleMenuEntity.getMenuId(),roleIds);
            });
        }
        return menuRoleIdsMap;
    }

    public void writeMenuRoleIdsCache(String menuId,Set<String> roleIds){
        if(StrUtil.isNotBlank(menuId) && CollUtil.isNotEmpty(roleIds)){
            redisTemplate.opsForHash().put(MENU_ROLE_CACHE_HASH_KEY,menuId,StrUtil.join(StringPool.COMMA,roleIds));
        }
    }

    public Set<String> readMenuRoleIdsCache(String menuId){
        Object object = redisTemplate.opsForHash().get(MENU_ROLE_CACHE_HASH_KEY, menuId);
        if(Objects.isNull(object)){
            return null;
        }
        return Sets.newHashSet(String.valueOf(object).split(StringPool.COMMA));
    }


    public void removeMenuRoleIds(String menuId,Set<String> roleIds){
        Object object = redisTemplate.opsForHash().get(MENU_ROLE_CACHE_HASH_KEY, menuId);
        if(Objects.nonNull(object)){
            Set<String> cacheRoleIds = Sets.newHashSet(String.valueOf(object).split(StringPool.COMMA));
            roleIds.stream().forEach(roleId->{
                if(cacheRoleIds.contains(roleId)){
                    cacheRoleIds.remove(roleId);
                }
            });
            writeMenuRoleIdsCache(menuId,cacheRoleIds);
        }
    }

}
