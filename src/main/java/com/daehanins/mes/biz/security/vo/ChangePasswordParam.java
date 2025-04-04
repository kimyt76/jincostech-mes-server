package com.daehanins.mes.biz.security.vo;

import lombok.Data;

@Data
public class ChangePasswordParam {

    private String userId;

    private String oldPassword;

    private String newPassword;
}
