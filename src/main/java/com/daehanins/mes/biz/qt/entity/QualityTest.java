package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 품질검사QualityTest 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("quality_test")
public class QualityTest extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 품질검사번호
     */
    @TableId
    private String qualityTestId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 시험번호
     */
    private String testNo;

    /**
     * 재검사여부
     */
    private String retestYn;

    /**
     * 재검사일련번호
     */
    private String retestSerNo;

    /**
     * 요청일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate reqDate;

    /**
     * 요청자
     */
    private String reqMemberCd;

    /**
     * 구역(공장)코드
     */
    private String areaCd;

    /**
     * 창고(검체채취)코드
     */
    private String storageCd;

    /**
     * 요청량
     */
    private BigDecimal reqQty;

    /**
     * 시험일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate testDate;

    /**
     * 판정일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate confirmDate;

    /**
     * 지시자
     */
    private String orderMemberCd;

    /**
     * 검체채취자
     */
    private String sampleMemberCd;

    /**
     * 검사자
     */
    private String testMemberCd;

    /**
     * 확인자
     */
    private String confirmMemberCd;

    /**
     * 검체채쥐량
     */
    private BigDecimal sampleQty;

    /**
     * 검체채쥐량(2)
     */
    private BigDecimal testQty;

    /**
     * 시험상태
     */
    private String testState;

    /**
     * 판정상태
     */
    private String passState;

    /**
     * tran반영여부
     */
    private String tranYn;

    /**
     * 자재tranId
     */
    private String matTranId;

    /**
     * 비고
     */
    private String memo;

}
