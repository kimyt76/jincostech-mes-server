package com.daehanins.mes.biz.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.daehanins.mes.biz.security.entity.Role;
import com.daehanins.mes.biz.security.entity.User;
import com.daehanins.mes.biz.security.entity.UserRole;
import com.daehanins.mes.biz.security.mapper.UsersMapper;
import com.daehanins.mes.biz.security.service.IRolesService;
import com.daehanins.mes.biz.security.service.IUserRolesService;
import com.daehanins.mes.biz.security.service.IUsersService;
import com.daehanins.mes.biz.security.vo.RoleVo;
import com.daehanins.mes.common.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 사용자Users 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, User> implements IUsersService {

    @Autowired
    IRolesService rolesService;

    @Autowired
    IUserRolesService userRolesService;


    public User findByUserId(String userId) {
        User user = this.getOne(new QueryWrapper<User>().eq("user_id", userId));
        Set<Role> roles = this.getUserRoles(user.getUsername());
        user.setRoles(roles);
        return user;
    }

    public User findByUserName(String userName) {
        User user = this.getOne(new QueryWrapper<User>().eq("user_id", userName));
        Set<Role> roles = this.getUserRoles(userName);
        user.setRoles(roles);
        return user;
    }

    public boolean existsByUserName(String userName) {
        int count = this.count(new QueryWrapper<User>().eq("user_id", userName));
        return count > 0;
    }

    public boolean existsByEmail(String email) {
        int count = this.count(new QueryWrapper<User>().eq("email", email));
        return count > 0;
    }

    public Set<Role> getUserRoles(String userId) {
        List<Role> roles = this.baseMapper.getUserRoles(userId);

//        List<Role> roles = new ArrayList<>();
//        for(RoleVo roleVo: roleVos) {
//            Role role = new Role();
//            role.setId(roleVo.getRoleId());
//            role.setName(roleVo.getRoleName());
//            roles.add(role);
//        }

        return ObjectUtil.convertListToSet(roles);
    }

    public boolean saveWithRoles(User user, Set<Role> roles) {
        String userId = user.getUserId();
        roles.forEach(role -> {
            this.rolesService.saveOrUpdate(role);
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(role.getId());
            this.userRolesService.save(userRole);
        });

        return SqlHelper.retBool(this.getBaseMapper().insert(user));
    }

    public boolean saveWithDefault(User user) {
        String userId = user.getUserId();
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId("1");
        this.userRolesService.save(userRole);

        return SqlHelper.retBool(this.getBaseMapper().insert(user));
    }

    public void insertUserMenus(String userId) {
        this.baseMapper.insertUserMenus(userId);
    }

//    public Set<String> getStrRolesByUsername(String userName) {
//        List<Role> roles = this.baseMapper.getUserRoles(userName);
//
//        Set<String> strRoles = new HashSet<>();
//        for (Role role : roles)
//            strRoles.add(role.getName());
//
//        return strRoles;
//    }
}
