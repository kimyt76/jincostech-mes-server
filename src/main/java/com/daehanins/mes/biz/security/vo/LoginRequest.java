package com.daehanins.mes.biz.security.vo;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
