package com.daehanins.mes.common.utils;

import com.daehanins.mes.biz.security.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUtil {

    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encodePassword(String text) {
        return encoder.encode(text);
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "";
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            username = userDetails.getUsername();
        } else if ("anonymousUser".equals(authentication.getPrincipal())) {
            username = "anonymous";
        } else {
            username = "system";
        }
        return username;
    }

    public static String getMemberCd() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String memberCd = "";
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            memberCd = userDetails.getMemberCd();
        } else if ("anonymousUser".equals(authentication.getPrincipal())) {
            memberCd = "anonymous";
        } else {
            memberCd = "system";
        }
        return memberCd;
    }

    public static void main(String[] args) {
        String basicPw = "1234";
        System.out.println("[" + encodePassword(basicPw) + "]");
    }

}
