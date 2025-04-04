package com.daehanins.mes.biz.security.service.impl;

import com.daehanins.mes.biz.security.entity.Role;
import com.daehanins.mes.biz.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsersServiceImpl usersService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersService.findByUserName(username);
        Set<Role> roles = usersService.getUserRoles(username);
        user.setRoles(roles);
        if (!usersService.existsByUserName(username)) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }

        return UserDetailsImpl.build(user);
    }

}