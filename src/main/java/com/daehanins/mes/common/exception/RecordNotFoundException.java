package com.daehanins.mes.common.exception;

import lombok.EqualsAndHashCode;

/**
 * @author jeonsj
 */
@EqualsAndHashCode(callSuper = false)
public class RecordNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 1137043513749947259L;

    public RecordNotFoundException(String exception) {
        super(exception);
    }
}