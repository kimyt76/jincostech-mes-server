package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
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
 * 품목시험번호ItemTestNo 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("item_test_no")
public class ItemTestNo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 시험번호
     */
    @TableId
    private String testNo;

    /**
     * 일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate createDate;

    /**
     * 구역(공장)구분
     */
    private String areaGb;

    /**
     * 품목구분
     */
    private String itemGb;

    /**
     * 일련번호
     */
    private Integer serNo;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 로트번호
     */
    private String lotNo;

    /**
     * 제조번호
     */
    private String prodNo;


    /**
     * 수량
     */
    private BigDecimal qty;

    /**
     * 공급거래처코드
     */
    private String customerCd;

    /**
     * 사용기한
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate expiryDate;

    /**
     * 보관기한
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate shelfLife;

    /**
     * 시험상태
     */
    private String testState;

    /**
     * 판정상태
     */
    private String passState;

    /**
     * 종결여부
     */
    private String endYn;

 }
