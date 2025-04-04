package com.daehanins.mes.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jeonsj
 * REST 반환 데이터 표준
 */
@Data
public class RestResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 성공 여부
     */
    private boolean success;

    /**
     * 상태 코드  0~999: http status , 1000~ : 시스템 지정코드
     */
    private int code;

    /**
     * 메시지
     */
    private String message;

    /**
     * 타임스탬프
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 결과 오브젝트  or 에러시 상세 원인 List<String>
     */
    private T result;
}
