package com.daehanins.mes.biz.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.security.entity.Role;
import com.daehanins.mes.biz.security.entity.User;

import java.util.Set;

/**
 * <p>
 * 사용자Users 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
public interface IUsersService extends IService<User> {

    User findByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean saveWithRoles(User user, Set<Role> roles);

    boolean saveWithDefault(User user);

    Set<Role> getUserRoles(String userId);

    void insertUserMenus(String userId);

//    Set<String> getStrRolesByUsername(String userName);
}
