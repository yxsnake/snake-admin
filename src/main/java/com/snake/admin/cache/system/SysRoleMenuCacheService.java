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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class SysRoleMenuCacheService {

    private final static String ROLE_MENU_ID_LIST = "role:menu_id:list";
    private final static String ROLE_MENU_CACHE_HASH_KEY = String.format(Cons.CACHE_REDIS_HASH_FORMAT,Cons.CACHE_PREFIX,ROLE_MENU_ID_LIST);

    private final SysRoleMenuEntityMapper sysRoleMenuEntityMapper;

    private final RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        log.info("=============================> 开始初始化角色与菜单关系 <============================================");
        Map<String, Set<String>> menuRoleIdsMap = queryRoleMenuIdsMap();
        redisTemplate.opsForHash().entries(ROLE_MENU_CACHE_HASH_KEY).forEach((k,v)->{
            removeRoleMenuIds(String.valueOf(k),Sets.newHashSet(String.valueOf(v).split(StringPool.COMMA)));
        });
        for (Map.Entry<String, Set<String>> entry : menuRoleIdsMap.entrySet()) {
            writeRoleMenuIdsCache(entry.getKey(),entry.getValue());
        }

        log.info("=============================> 结束初始化菜单与角色关系 <============================================");
    }


    private Map<String, Set<String>> queryRoleMenuIdsMap(){
        Map<String, Set<String>> roleMenuIdsMap = Maps.newHashMap();
        List<SysRoleMenuEntity> roleMenuEntities = sysRoleMenuEntityMapper.selectList(Wrappers.lambdaQuery(SysRoleMenuEntity.class));
        if(CollUtil.isNotEmpty(roleMenuEntities)){
            roleMenuEntities.stream().forEach(sysRoleMenuEntity -> {
                Set<String> menuIds = roleMenuIdsMap.get(sysRoleMenuEntity.getRoleId());
                if(Objects.isNull(menuIds)){
                    menuIds = Sets.newHashSet();
                }
                menuIds.add(sysRoleMenuEntity.getMenuId());
                roleMenuIdsMap.put(sysRoleMenuEntity.getRoleId(),menuIds);
            });
        }
        return roleMenuIdsMap;
    }

    public void writeRoleMenuIdsCache(String roleId,Set<String> menuIds){
        if(StrUtil.isNotBlank(roleId) && CollUtil.isNotEmpty(menuIds)){
            redisTemplate.opsForHash().put(ROLE_MENU_CACHE_HASH_KEY,roleId,StrUtil.join(StringPool.COMMA,menuIds));
        }
    }

    public Set<String> readRoleMenuIdsCache(String roleId){
        Object object = redisTemplate.opsForHash().get(ROLE_MENU_CACHE_HASH_KEY, roleId);
        if(Objects.isNull(object)){
            return null;
        }
        return Sets.newHashSet(String.valueOf(object).split(StringPool.COMMA));
    }

    public Set<String> readRoleMenuIdsCache(Set<String> roleIds){
        Set<String> menuIds = Sets.newHashSet();
        for (String roleId : roleIds) {
            Set<String> menuIdsCache = readRoleMenuIdsCache(roleId);
            menuIds.addAll(menuIdsCache);
        }
        return menuIds;
    }

    public void removeRoleMenuIds(String roleId,Set<String> menuIds){

    }
}
