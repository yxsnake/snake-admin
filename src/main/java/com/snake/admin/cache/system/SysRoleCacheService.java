package com.snake.admin.cache.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.snake.admin.common.Cons;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.model.system.entity.SysRoleEntity;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class SysRoleCacheService {

    private final static String ROLE_LIST = "role:list";
    private final static String ROLE_CACHE_HASH_KEY = String.format(Cons.CACHE_REDIS_HASH_FORMAT,Cons.CACHE_PREFIX,ROLE_LIST);

    private final SysRoleEntityMapper sysRoleEntityMapper;

    private final RedisTemplate redisTemplate;


    @PostConstruct
    public void init(){
        log.info("=============================> 开始初始化角色 <============================================");
        List<SysRoleEntity> sysRoleEntities = this.queryAll();
        // 删除 原 缓存中的所有菜单数据
        redisTemplate.opsForHash().entries(ROLE_CACHE_HASH_KEY).forEach((k,v)->{
            removeRoleCache(String.valueOf(k));
        });
        // 如果 从数据库中查询到菜单则进行缓存
        if(CollUtil.isNotEmpty(sysRoleEntities)){
            sysRoleEntities.stream().forEach(sysRoleEntity -> {
                writeRoleToCache(sysRoleEntity);
            });
        }
        log.info("=============================> 结束初始化角色 共计菜单数据 :{} 条<============================================",sysRoleEntities.size());

    }

    /**
     * 写入缓存
     * @param sysRoleEntity
     */
    public void writeRoleToCache(SysRoleEntity sysRoleEntity){
        if(Objects.isNull(sysRoleEntity)){
            return;
        }
        String hKey = sysRoleEntity.getId();
        redisTemplate.opsForHash().put(ROLE_CACHE_HASH_KEY,hKey, sysRoleEntity.toString());
    }

    /**
     * 读取缓存
     * @param roleId
     * @return
     */
    public SysRoleEntity readRoleFormCache(String roleId){
        if(StrUtil.isBlank(roleId)){
            return null;
        }
        Object object = redisTemplate.opsForHash().get(ROLE_CACHE_HASH_KEY, roleId);
        if(Objects.nonNull(object)){
            return JsonUtils.jsonCovertToObject(String.valueOf(object),SysRoleEntity.class);
        }
        return null;
    }

    public List<SysRoleEntity> readRoleFormCache(Collection<String> roleIds){
        List<SysRoleEntity> roles = Lists.newArrayList();
        if(CollUtil.isEmpty(roleIds)){
            return null;
        }
        for (String roleId : roleIds) {
            SysRoleEntity roleEntity = readRoleFormCache(roleId);
            roles.add(roleEntity);
        }
        return roles;
    }

    public void removeRoleCache(String roleId){
        String hKey = roleId;
        redisTemplate.opsForHash().delete(ROLE_CACHE_HASH_KEY,hKey);
    }
    private List<SysRoleEntity> queryAll(){
        return sysRoleEntityMapper.selectList(Wrappers.lambdaQuery(SysRoleEntity.class));
    }


    public List<SysRoleEntity> getAllRoleList() {
        List<SysRoleEntity> roles = Lists.newArrayList();
        redisTemplate.opsForHash().entries(ROLE_CACHE_HASH_KEY).forEach((k,v)->{
            if(Objects.nonNull(v)){
                String roleJson = String.valueOf(v);
                SysRoleEntity roleEntity = JsonUtils.jsonCovertToObject(roleJson, SysRoleEntity.class);
                roles.add(roleEntity);
            }
        });
        return roles;
    }
}
