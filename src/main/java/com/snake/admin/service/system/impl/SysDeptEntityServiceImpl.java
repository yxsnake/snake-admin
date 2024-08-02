package com.snake.admin.service.system.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snake.admin.common.enums.SysDeptStatusEnum;
import com.snake.admin.mapper.system.SysDeptEntityMapper;
import com.snake.admin.mapper.system.SysUserEntityMapper;
import com.snake.admin.model.system.dto.SysDeptDTO;
import com.snake.admin.model.system.entity.SysDeptEntity;
import com.snake.admin.model.system.entity.SysUserEntity;
import com.snake.admin.model.system.form.CreateSysDeptForm;
import com.snake.admin.model.system.form.ModifySysDeptForm;
import com.snake.admin.service.system.SysDeptEntityService;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptEntityServiceImpl extends ServiceImpl<SysDeptEntityMapper, SysDeptEntity> implements SysDeptEntityService {

    private final SysUserEntityMapper sysUserEntityMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSysDeptForm form) {
        // 校验部门是否已存在
        SysDeptEntity deptEntity = this.lambdaQuery().eq(SysDeptEntity::getName, form.getName()).list().stream().findFirst().orElse(null);
        BizAssert.isTrue("名称已存在",Objects.nonNull(deptEntity));

        String id = IdWorker.getIdStr();
        String parentId = form.getParentId();
        SysDeptEntity sysDeptEntity = form.convert(SysDeptEntity.class);
        sysDeptEntity.setId(id);
        if(StrUtil.isNotBlank(parentId) && SysDeptEntity.ROOT.equals(parentId)){
            long count = this.lambdaQuery().eq(SysDeptEntity::getId, parentId).list().stream().count();
            BizAssert.isTrue("上级部门不存在",count == 0);
        }
        if(StrUtil.isBlank(parentId)){
            parentId = SysDeptEntity.ROOT;
        }
        sysDeptEntity.setParentId(parentId);
        SysDeptStatusEnum sysDeptStatusEnum = SysDeptStatusEnum.getInstance(sysDeptEntity.getStatus());
        if(Objects.isNull(sysDeptStatusEnum)){
            sysDeptEntity.setStatus(SysDeptStatusEnum.NORMAL.getValue());
        }
        if(Objects.isNull(sysDeptEntity.getSort())){
            sysDeptEntity.setSort(DateUtil.date().getTime());
        }
        this.save(sysDeptEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifySysDeptForm form) {
        String id = form.getId();

        SysDeptEntity deptEntity = this.lambdaQuery().eq(SysDeptEntity::getName, form.getName())
                .ne(SysDeptEntity::getId,id)
                .list().stream().findFirst().orElse(null);
        BizAssert.isTrue("名称已存在",Objects.nonNull(deptEntity));

        String parentId = form.getParentId();
        SysDeptEntity sysDeptEntity = this.getBaseMapper().selectById(id);
        BizAssert.isTrue("部门信息不存在",Objects.isNull(sysDeptEntity));
        if(StrUtil.isBlank(parentId)){
            parentId = SysDeptEntity.ROOT;
        }
        BeanUtils.copyProperties(form,sysDeptEntity);
        sysDeptEntity.setParentId(parentId);
        this.getBaseMapper().updateById(sysDeptEntity);
    }

    @Override
    public SysDeptDTO detail(String id) {
        SysDeptEntity sysDeptEntity = this.getBaseMapper().selectById(id);
        if(Objects.isNull(sysDeptEntity)){
            return null;
        }
        return sysDeptEntity.convert(SysDeptDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        SysDeptEntity sysDeptEntity = this.getBaseMapper().selectById(id);
        BizAssert.isTrue("部门不存在", Objects.isNull(sysDeptEntity));
        //校验部门是否已关联用户
        Boolean existDeptBindUser = sysUserEntityMapper.selectCount(
                Wrappers.lambdaQuery(SysUserEntity.class)
                        .eq(SysUserEntity::getDeptId,id)
        )>0;
        BizAssert.isTrue("当前部门下存在用户，不允许删除",existDeptBindUser);
        this.getBaseMapper().deleteById(id);
    }
}
