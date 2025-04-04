package com.daehanins.mes.biz.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.security.entity.Role;
import com.daehanins.mes.biz.security.entity.User;
import com.daehanins.mes.biz.security.mapper.RolesMapper;
import com.daehanins.mes.biz.security.service.IRolesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 역할Roles 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Role> implements IRolesService {

    public Role findByName(String roleName) {
        return this.getOne(new QueryWrapper<Role>().eq("role_name", roleName));
    }
}
