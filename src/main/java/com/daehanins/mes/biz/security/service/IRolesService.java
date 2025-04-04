package com.daehanins.mes.biz.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.security.entity.Role;

/**
 * <p>
 * 역할Roles 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
public interface IRolesService extends IService<Role> {

    Role findByName(String name);
}
