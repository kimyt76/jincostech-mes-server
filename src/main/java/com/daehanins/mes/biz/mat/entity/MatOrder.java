package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 자재지시(요청)MatOrder 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_order")
public class MatOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 자재지시(요청)id
     */
    @TableId
    private String matOrderId  = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 자재거래유형코드
     */
    private String tranCd;

    /**
     * 지시(요청)일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    /**
     * 순번
     */
    private Integer serNo;

    /**
     * 지시상태
     */
    private String orderState;

    /**
     * 적요
     */
    private String memo;

    /**
     * 납기일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate deliveryDate;

    /**
     * 구역(공장)
     */
    private String areaCd;

    /**
     * 소스창고코드
     */
    private String srcStorageCd;

    /**
     * 대상창고코드
     */
    private String destStorageCd;

    /**
     * 거래처코드
     */
    private String customerCd;

    /**
     * 담당자코드
     */
    private String memberCd;

    /**
     * 제조사
     */
    private String maker;

    /**
     * 거래처담당자명
     */
    private String customerManager;

    /**
     * 부가세유형
     */
    private String vatType;

    /**
     * 자재구분코드
     */
    private String itemTypeCd;

    /**
     * 메일여부
     */
    private String mailYn;

    /**
     * 인쇄여부
     */
    private String printYn;

    /**
     * 종료여부
     */
    private String endYn;

}
