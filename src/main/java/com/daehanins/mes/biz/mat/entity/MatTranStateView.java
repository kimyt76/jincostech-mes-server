package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
* <p>
* MatTranStateView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-07-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_tran_state_view")
public class MatTranStateView {

    @TableId
    private String matTranItemId;

    private String matTranId;

    private String tranCd;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate tranDate;

    private Integer serNo;

    private String tranNo;

    private String areaCd;
    private String areaName;

    private String storageCd;
    private String storageName;

    private String memberCd;
    private String memberName;

    private String itemTypeCd;
    private String itemCd;
    private String itemName;

    private String spec;

    private String lotNo;

    private String testNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate expiryDate;

    private String orderType;

    private String customerCd;
    private String customerName;

    private BigDecimal price;
    private BigDecimal qty;
    private BigDecimal supplyAmt;
    private BigDecimal vat;
    private BigDecimal totalAmt;

    private String memo;

    private String confirmState;
    private String testState;
    private String passState;

    private String confirmStateName;
    private String testStateName;
    private String passStateName;

}
