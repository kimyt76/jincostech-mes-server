package com.daehanins.mes.biz.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.security.entity.Role;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 역할 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@Repository
public interface RolesMapper extends BaseMapper<Role> {

}
