package com.daehanins.mes.biz.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.security.entity.Role;
import com.daehanins.mes.biz.security.entity.User;
import com.daehanins.mes.biz.security.vo.RoleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 사용자 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@Repository
public interface UsersMapper extends BaseMapper<User> {

    List<Role> getUserRoles(String userId);

    List<RoleVo> getUserRoleVos(String userId);

    void insertUserMenus(String userId);
}
