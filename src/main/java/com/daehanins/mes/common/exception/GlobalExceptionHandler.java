package com.daehanins.mes.common.exception;

import com.daehanins.mes.biz.common.schedule.BatchSchedule;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author jeonsj
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public final ResponseEntity<RestResponse<String>> handleBizExceptions(BizException ex, WebRequest request) {

        logger.debug(ex.getStackTrace().toString());

        RestResponse<String> response = new RestUtil<String>().setError(2000, ex.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<RestResponse<String>> handleAccessDeniedExceptions(AccessDeniedException ex, WebRequest request) {

        logger.debug(ex.getStackTrace().toString());

        RestResponse<String> response = new RestUtil<String>().setError(401, ex.getMessage(), ex.getLocalizedMessage());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<RestResponse<String>> handleAllExceptions(Exception ex, WebRequest request) {

        logger.debug(ex.getStackTrace().toString());

        RestResponse<String> response = new RestUtil<String>().setError(500, ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
