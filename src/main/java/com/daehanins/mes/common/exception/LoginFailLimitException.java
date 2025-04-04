package com.daehanins.mes.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @author jeonsj
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class LoginFailLimitException extends InternalAuthenticationServiceException {

    private String msg;

    public LoginFailLimitException(String msg){
        super(msg);
        this.msg = msg;
    }

    public LoginFailLimitException(String msg, Throwable t) {
        super(msg, t);
        this.msg = msg;
    }
}
