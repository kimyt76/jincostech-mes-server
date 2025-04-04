package com.daehanins.mes.biz.security.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.daehanins.mes.biz.adm.entity.UserProgramView;
import com.daehanins.mes.biz.adm.service.IUserProgramViewService;
import com.daehanins.mes.biz.security.entity.ERole;
import com.daehanins.mes.biz.security.entity.Role;
import com.daehanins.mes.biz.security.entity.User;
import com.daehanins.mes.biz.security.jwt.JwtUtils;
import com.daehanins.mes.biz.security.service.IRolesService;
import com.daehanins.mes.biz.security.service.IUsersService;
import com.daehanins.mes.biz.security.service.impl.UserDetailsImpl;
import com.daehanins.mes.biz.security.service.impl.UsersServiceImpl;
import com.daehanins.mes.biz.security.vo.JwtResponse;
import com.daehanins.mes.biz.security.vo.LoginRequest;
import com.daehanins.mes.biz.security.vo.MessageResponse;
import com.daehanins.mes.biz.security.vo.SignupRequest;
import com.daehanins.mes.common.exception.BizException;
import com.daehanins.mes.common.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUsersService usersService;

    @Autowired
    IRolesService rolesService;

    @Autowired
    IUserProgramViewService userProgramViewService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            /* 사용정지된 아이디 처리 */
            User user = usersService.findByUserName(loginRequest.getUsername());
            if (user.getUseYn().equals("N")){
                throw new BizException("사용 정지된 아이디");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)    // item -> item.getAuthority()
                    .collect(Collectors.toList());

            QueryWrapper<UserProgramView> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", loginRequest.getUsername());
            queryWrapper.orderByAsc("display_order");
            List<UserProgramView> userPrograms = userProgramViewService.list(queryWrapper);

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getEmail(),
                    userDetails.getMemberCd(),
                    userDetails.getMemberName(),
                    roles,
                    userPrograms
                    ));
        } catch (AuthenticationException ex) {
            throw new BizException("로그인 오류: 아이디, 비밀번호를 정확히 확인하세요.(" + ex.getMessage() + ")");
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            throw new BizException("로그인 오류: 권한체크중 오류 발생.(" + ex.getMessage() + ")");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (usersService.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (usersService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = rolesService.findByName(ERole.ROLE_USER.name());
            if (userRole == null) {
                throw new RuntimeException("Error: Role is not found.");
            }
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = rolesService.findByName(ERole.ROLE_ADMIN.name());
                        if (adminRole == null) {
                            throw new RuntimeException("Error: Role is not found.");
                        }
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = rolesService.findByName(ERole.ROLE_MODERATOR.name());
                        if (modRole == null) {
                            throw new RuntimeException("Error: Role is not found.");
                        }
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = rolesService.findByName(ERole.ROLE_USER.name());
                        if (userRole == null) {
                            throw new RuntimeException("Error: Role is not found.");
                        }
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        // 사용자 정보와 역할정보 같이 등록
        usersService.saveWithRoles(user, roles);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
