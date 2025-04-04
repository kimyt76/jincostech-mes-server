package com.daehanins.mes.biz.security.vo;

import com.daehanins.mes.biz.adm.entity.UserProgramView;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String email;

    private String memberCd;
    private String memberName;

    private List<String> roles;

    private List<UserProgramView> userPrograms;

    public JwtResponse(String accessToken,
                       String id,
                       String email,
                       String memberCd,
                       String memberName,
                       List<String> roles,
                       List<UserProgramView> userPrograms) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.memberCd = memberCd;
        this.memberName = memberName;
        this.roles = roles;
        this.userPrograms = userPrograms;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberCd() { return memberCd; }

    public void setMemberCd(String memberCd) { this.memberCd = memberCd; }

    public String getMemberName() { return memberName; }

    public void setMemberName(String memberName) { this.memberName = memberName; }

    public List<String> getRoles() {
        return roles;
    }

    public List<UserProgramView> getUserPrograms() {
        return userPrograms;
    }
}