package com.daehanins.mes.common.exception;

import java.util.List;

public class BizException extends RuntimeException {

    protected String details;

    public BizException(String message) {
        super(message);
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
