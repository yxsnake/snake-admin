package com.snake.admin.cache.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.snake.admin.common.Cons;
import com.snake.admin.mapper.system.SysMenuEntityMapper;
import com.snake.admin.model.system.entity.SysMenuEntity;
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
public class SysMenuCacheService {

    private final static String MENU_LIST = "menu:list";
    private final static String MENU_CACHE_HASH_KEY = String.format(Cons.CACHE_REDIS_HASH_FORMAT,Cons.CACHE_PREFIX,MENU_LIST);

    private final SysMenuEntityMapper sysMenuEntityMapper;

    private final RedisTemplate redisTemplate;


    @PostConstruct
    public void init(){
        log.info("=============================> 开始初始化菜单 <============================================");
        List<SysMenuEntity> sysMenuEntities = this.queryAll();
        // 删除 原 缓存中的所有菜单数据
        redisTemplate.opsForHash().entries(MENU_CACHE_HASH_KEY).forEach((k,v)->{
            removeMenuCache(String.valueOf(k));
        });
        // 如果 从数据库中查询到菜单则进行缓存
        if(CollUtil.isNotEmpty(sysMenuEntities)){
            sysMenuEntities.stream().forEach(sysMenuEntity -> {
                writeMenuToCache(sysMenuEntity);
            });
        }
        log.info("=============================> 结束初始化菜单 共计菜单数据 :{} 条(包含目录,菜单,iframe,外链,按钮)<============================================",sysMenuEntities.size());

    }

    /**
     * 写入缓存
     * @param sysMenuEntity
     */
    public void writeMenuToCache(SysMenuEntity sysMenuEntity){
        if(Objects.isNull(sysMenuEntity)){
            return;
        }
        String hKey = sysMenuEntity.getId();
        redisTemplate.opsForHash().put(MENU_CACHE_HASH_KEY,hKey, sysMenuEntity.toString());
    }

    /**
     * 读取缓存
     * @param menuId
     * @return
     */
    public SysMenuEntity readMenuFormCache(String menuId){
        if(StrUtil.isBlank(menuId)){
            return null;
        }
        Object object = redisTemplate.opsForHash().get(MENU_CACHE_HASH_KEY, menuId);
        if(Objects.nonNull(object)){
            return JsonUtils.jsonCovertToObject(String.valueOf(object),SysMenuEntity.class);
        }
        return null;
    }

    public List<SysMenuEntity> readMenuFormCache(Collection<String> menuIds){
        List<SysMenuEntity> menus = Lists.newArrayList();
        if(CollUtil.isEmpty(menuIds)){
            return null;
        }
        for (String menuId : menuIds) {
            SysMenuEntity menuEntity = readMenuFormCache(menuId);
            if(Objects.nonNull(menuEntity)){
                menus.add(menuEntity);
            }
        }
        return menus;
    }

    public void removeMenuCache(String menuId){
        String hKey = menuId;
        redisTemplate.opsForHash().delete(MENU_CACHE_HASH_KEY,hKey);
    }
    private List<SysMenuEntity> queryAll(){
        return sysMenuEntityMapper.selectList(Wrappers.lambdaQuery(SysMenuEntity.class));
    }

    public List<SysMenuEntity> getAllMenuList() {
        List<SysMenuEntity> menus = Lists.newArrayList();
        redisTemplate.opsForHash().entries(MENU_CACHE_HASH_KEY).forEach((k,v)->{
            String menuJson = String.valueOf(v);
            SysMenuEntity menuEntity = JsonUtils.jsonCovertToObject(menuJson, SysMenuEntity.class);
            menus.add(menuEntity);
        });
        return menus;
    }
}
