package com.snake.admin.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.common.enums.SysDeptStatusEnum;
import com.snake.admin.common.enums.SysUserDeletedEnum;
import com.snake.admin.common.enums.SysUserStatusEnum;
import com.snake.admin.common.utils.PwdUtil;
import com.snake.admin.mapper.system.SysRoleEntityMapper;
import com.snake.admin.mapper.system.SysUserEntityMapper;
import com.snake.admin.mapper.system.SysUserRoleEntityMapper;
import com.snake.admin.model.system.dto.SysRoleDTO;
import com.snake.admin.model.system.dto.SysUserDTO;
import com.snake.admin.model.system.entity.SysDeptEntity;
import com.snake.admin.model.system.entity.SysRoleEntity;
import com.snake.admin.model.system.entity.SysUserEntity;
import com.snake.admin.model.system.entity.SysUserRoleEntity;
import com.snake.admin.model.system.equal.QuerySysUserEqual;
import com.snake.admin.model.system.form.CreateSysUserForm;
import com.snake.admin.model.system.form.ModifySysUserForm;
import com.snake.admin.model.system.form.RestUsrPwdForm;
import com.snake.admin.model.system.form.UpdateSysUserStatusForm;
import com.snake.admin.model.system.fuzzy.QuerySysUserFuzzy;
import com.snake.admin.service.system.SysDeptEntityService;
import com.snake.admin.service.system.SysUserEntityService;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserEntityServiceImpl extends ServiceImpl<SysUserEntityMapper, SysUserEntity> implements SysUserEntityService {

    @Value("${rest.pwd.safe.code:888888}")
    private String restPwdSafeCode;
    
    private final SysDeptEntityService sysDeptEntityService;

    private final SysUserRoleEntityMapper sysUserRoleEntityMapper;

    private final SysRoleEntityMapper sysRoleEntityMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSysUserForm form) {
        SysUserEntity userEntity = form.convert(SysUserEntity.class);
        String userId = IdWorker.getIdStr();
        String deptId = form.getDeptId();
        String username = form.getUsername();
        String password = form.getPassword();
        String ciphertext = PwdUtil.ciphertext(username,password);
        String phone = form.getPhone();
        // 校验用户名是否已存在
        SysUserEntity sysUserEntity = this.lambdaQuery().eq(SysUserEntity::getUsername, username).list().stream().findFirst().orElse(null);
        BizAssert.isTrue("用户名已存在",Objects.nonNull(sysUserEntity));
        // 校验手机号是否唯一
        if(StrUtil.isNotBlank(phone)){
            sysUserEntity = this.lambdaQuery().eq(SysUserEntity::getPhone, phone).list().stream().findFirst().orElse(null);
            BizAssert.isTrue("手机号已被其他账号使用",Objects.nonNull(sysUserEntity));
        }
        userEntity.setId(userId);
        userEntity.setAvatar(SysUserEntity.DEFAULT_AVATAR);
        userEntity.setPassword(ciphertext);
        userEntity.setStatus(SysUserStatusEnum.NORMAL.getValue());
        // 校验 部门是否存在 或者是否已禁用
        SysDeptEntity sysDeptEntity = sysDeptEntityService.getBaseMapper().selectById(deptId);
        BizAssert.isTrue("部门信息不存在", Objects.isNull(sysDeptEntity));
        BizAssert.isTrue("部门已禁用不允许关联用户", SysDeptStatusEnum.DISABLE.getValue().equals(sysDeptEntity.getStatus()));
        this.save(userEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifySysUserForm form) {
        String userId = form.getId();
        String deptId = form.getDeptId();
        SysUserEntity sysUserEntity = this.getBaseMapper().selectById(userId);
        BizAssert.isTrue("用户信息不存在",Objects.isNull(sysUserEntity));
        // 校验 部门是否存在
        SysDeptEntity sysDeptEntity = sysDeptEntityService.getBaseMapper().selectById(deptId);
        BizAssert.isTrue("部门信息不存在", Objects.isNull(sysDeptEntity));
        BizAssert.isTrue("部门已禁用不允许关联用户", SysDeptStatusEnum.DISABLE.getValue().equals(sysDeptEntity.getStatus()));
        BeanUtils.copyProperties(form,sysUserEntity);
        this.getBaseMapper().updateById(sysUserEntity);
    }

    @Override
    public SysUserDTO detail(String id) {
        SysUserEntity sysUserEntity = this.getBaseMapper().selectById(id);
        if(Objects.isNull(sysUserEntity)){
            return null;
        }
        SysUserDTO sysUserDTO = sysUserEntity.convert(SysUserDTO.class);
        List<SysUserRoleEntity> userRoleEntities = sysUserRoleEntityMapper.selectList(Wrappers.lambdaQuery(SysUserRoleEntity.class)
                .eq(SysUserRoleEntity::getUserId, id));
        if(CollUtil.isNotEmpty(userRoleEntities)){
            List<String> roleIds = userRoleEntities.stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toList());
            List<SysRoleEntity> sysRoleEntities = sysRoleEntityMapper.selectBatchIds(roleIds);
            sysUserDTO.setRoleList(sysRoleEntities.stream().map(item->item.convert(SysRoleDTO.class)).collect(Collectors.toList()));
            sysUserDTO.setRoles(sysRoleEntities.stream().map(SysRoleEntity::getCode).collect(Collectors.toList()));
        }
        return sysUserDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upateStatus(UpdateSysUserStatusForm form) {
        SysUserEntity sysUserEntity = this.getBaseMapper().selectById(form.getId());
        BizAssert.isTrue("用户信息不存在",Objects.isNull(sysUserEntity));
        sysUserEntity.setStatus(form.getStatus());
        this.getBaseMapper().updateById(sysUserEntity);
    }

    @Override
    public IPage<SysUserDTO> pageList(QueryFilter<QuerySysUserEqual, QuerySysUserFuzzy> queryFilter) {
        QuerySysUserEqual equalsQueries = QueryFilter.getEqualsQueries(QuerySysUserEqual.class, queryFilter.getEqualsQueries());
        BizAssert.isTrue("请选择一个部门",StrUtil.isBlank(equalsQueries.getDeptId()));
        QuerySysUserFuzzy fuzzyQueries = QueryFilter.getFuzzyQueries(QuerySysUserFuzzy.class, queryFilter.getFuzzyQueries());
        return this.page(new Page<>(queryFilter.getPageNum(),queryFilter.getPageSize()),
                Wrappers.lambdaQuery(SysUserEntity.class)
                        .ne(SysUserEntity::getUsername,SysUserEntity.SUPPER_ACCOUNT)
                        .eq(SysUserEntity::getDeptId,equalsQueries.getDeptId())
                        .eq(Objects.nonNull(equalsQueries.getStatus()),SysUserEntity::getStatus,equalsQueries.getStatus())
                        .like(StrUtil.isNotBlank(fuzzyQueries.getUsername()),SysUserEntity::getUsername,fuzzyQueries.getUsername())
                        .like(StrUtil.isNotBlank(fuzzyQueries.getPhone()),SysUserEntity::getPhone,fuzzyQueries.getPhone())
        ).convert(item->item.convert(SysUserDTO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restPassword(RestUsrPwdForm form) {
        BizAssert.isTrue("口令错误",!restPwdSafeCode.equals(form.getSafeCode()));
        SysUserEntity sysUserEntity = this.getBaseMapper().selectById(form.getUserId());
        BizAssert.isTrue("用户不存在",Objects.isNull(sysUserEntity));
        BizAssert.isTrue("超管账号密码不允许重置",SysUserEntity.SUPPER_ACCOUNT.equals(sysUserEntity.getUsername()));
        String cipherTexted = PwdUtil.ciphertext(sysUserEntity.getUsername(), sysUserEntity.getUsername());
        sysUserEntity.setPassword(cipherTexted);
        this.getBaseMapper().updateById(sysUserEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUser(String userIds) {
        List<String> userIdList = Arrays.asList(userIds.split(StringPool.COMMA));
        List<SysUserEntity> list = this.lambdaQuery().in(SysUserEntity::getId,userIdList)
                .ne(SysUserEntity::getUsername, SysUserEntity.SUPPER_ACCOUNT)
                .list();
        if(CollUtil.isNotEmpty(list)){
            list.stream().forEach(sysUserEntity -> {
                sysUserEntity.setDeleted(SysUserDeletedEnum.DELETED.getValue());
                this.getBaseMapper().updateById(sysUserEntity);
            });
        }
    }
}
