package com.snake.admin.cache.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.snake.admin.common.Cons;
import com.snake.admin.mapper.system.SysUserRoleEntityMapper;
import com.snake.admin.model.system.entity.SysUserRoleEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SysUserRoleCacheService {

    private final static String USER_ROLE_ID_LIST = "user:role_id:list";
    private final static String USER_ROLE_CACHE_HASH_KEY = String.format(Cons.CACHE_REDIS_HASH_FORMAT,Cons.CACHE_PREFIX,USER_ROLE_ID_LIST);

    private final SysUserRoleEntityMapper sysUserRoleEntityMapper;

    private final RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        log.info("=============================> 开始初始化用户角色关系 <============================================");
        Map<String, Set<String>> userRoleIdsMap = this.queryUserRoleIdsMap();
        if(CollUtil.isNotEmpty(userRoleIdsMap)){
            for (Map.Entry<String, Set<String>> entry : userRoleIdsMap.entrySet()) {
                String userId = entry.getKey();
                Set<String> roleIds = entry.getValue();
                writeUserRoleIdCache(userId,roleIds);
            }
        }
        log.info("=============================> 结束初始化用户角色关系 <============================================");
    }

    private Map<String, Set<String>> queryUserRoleIdsMap(){
        Map<String, Set<String>> userRoleIdsMap = Maps.newHashMap();
        List<SysUserRoleEntity> userRoleEntities = sysUserRoleEntityMapper.selectList(Wrappers.lambdaQuery(SysUserRoleEntity.class));
        if(CollUtil.isNotEmpty(userRoleEntities)){
            userRoleEntities.stream().forEach(sysUserRoleEntity -> {
                Set<String> roleIds = userRoleIdsMap.get(sysUserRoleEntity.getUserId());
                if(Objects.isNull(roleIds)){
                    roleIds = Sets.newHashSet();
                }
                roleIds.add(sysUserRoleEntity.getRoleId());
                userRoleIdsMap.put(sysUserRoleEntity.getUserId(),roleIds);
            });
        }
        return userRoleIdsMap;
    }

    /**
     * 用户角色关系写入缓存
     * @param userId
     * @param roleIds
     */
    public void writeUserRoleIdCache(String userId,Set<String> roleIds){
        redisTemplate.opsForHash().put(USER_ROLE_CACHE_HASH_KEY,userId, StrUtil.join(StringPool.COMMA,roleIds));
    }

    public Set<String> readUserRoles(String userId){
        Object object = redisTemplate.opsForHash().get(USER_ROLE_CACHE_HASH_KEY, userId);
        if(Objects.isNull(object)){
            return null;
        }
        return Sets.newHashSet(String.valueOf(object).split(StringPool.COMMA));
    }

    /**
     * 删除用户与角色关系的缓存
     * @param userId
     * @param roleIds
     */
    public void removeUserRole(String userId, Collection<String> roleIds){
        Set<String> cacheRoleIds = readUserRoles(userId);
        if(CollUtil.isNotEmpty(cacheRoleIds) && CollUtil.isNotEmpty(roleIds)){
            for (String roleId : roleIds) {
                cacheRoleIds.remove(roleId);
            }
            writeUserRoleIdCache(userId,cacheRoleIds);
        }
    }
}
