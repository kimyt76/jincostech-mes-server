package com.daehanins.mes.biz.security.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.vo.MatOrderSaveWithItems;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.security.entity.ERole;
import com.daehanins.mes.biz.security.entity.Role;
import com.daehanins.mes.biz.security.entity.User;
import com.daehanins.mes.biz.security.entity.UserView;
import com.daehanins.mes.biz.security.service.IRolesService;
import com.daehanins.mes.biz.security.service.IUserViewService;
import com.daehanins.mes.biz.security.service.IUsersService;
import com.daehanins.mes.biz.security.service.impl.UserDetailsImpl;
import com.daehanins.mes.biz.security.vo.ChangePasswordParam;
import com.daehanins.mes.biz.security.vo.JwtResponse;
import com.daehanins.mes.common.exception.BizException;
import com.daehanins.mes.common.utils.AuthUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.object.UpdatableSqlQuery;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 사용자Users Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/adm/users")
public class UsersController extends BaseController<User, UserView, String> {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IUserViewService userViewService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IRolesService rolesService;

    @Override
    public IUsersService getTableService() {
        return this.usersService;
    }

    @Override
    public IUserViewService getViewService() {
    return this.userViewService;
    }

    @RequestMapping(value = "/changePassword", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<String> changePassword(@RequestBody ChangePasswordParam requestParam){

        String userId = requestParam.getUserId();

        // 현재 비밀번호가 일치하는지 확인
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestParam.getUserId(), requestParam.getOldPassword()));

        } catch (AuthenticationException ex) {
            throw new BizException("현재 비밀번호를 정확히 확인하세요.(" + ex.getMessage() + ")");
        }

        this.usersService.update(new UpdateWrapper<User>()
                .eq("user_id", userId)
                .set("password", AuthUtil.encodePassword(requestParam.getNewPassword()))
        );

        String message = "OK";
        return new RestUtil<String>().setData(message);
    }

    @RequestMapping(value = "/changeAdmPassword", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<String> changeAdmPassword(@RequestBody ChangePasswordParam requestParam){

        String userId = requestParam.getUserId();

        this.usersService.update(new UpdateWrapper<User>()
                .eq("user_id", userId)
                .set("password", AuthUtil.encodePassword(requestParam.getNewPassword()))
        );

        String message = "OK";
        return new RestUtil<String>().setData(message);
    }

    @RequestMapping(value = "/saveNew", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<User> saveNew(@RequestBody User entity){
        User data;
        entity.setMemberCd(entity.getUserId());
        entity.setPassword(AuthUtil.encodePassword(entity.getPassword()));

        // 기본사용자로 등록
        Set<Role> roles = new HashSet<>();
        Role userRole = rolesService.findByName(ERole.ROLE_USER.name());
        roles.add(userRole);
        entity.setRoles(roles);
        // 사용자 정보와 역할정보 같이 등록
        if (usersService.saveWithRoles(entity, roles)) {
            data = entity;
            usersService.insertUserMenus(entity.getUserId());
        } else {
            data = null;
        }
        return new RestUtil<User>().setData(data);
    }
}
