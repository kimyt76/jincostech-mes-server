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
* MatOrderStateView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-07-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_order_state_view")
public class MatOrderStateView {

    @TableId
    private String matOrderItemId;

    private String matOrderId;

    private String tranCd;

    private String areaCd;
    private String areaName;

    private String orderNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    private Integer serNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate deliveryDate;

    private String itemTypeCd;
    private String itemTypeName;
    private String itemCd;
    private String itemName;

    private String unit;
    private String spec;

    private BigDecimal price;

    private BigDecimal orderQty;

    private BigDecimal supplyAmt;
    private BigDecimal vat;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate tranDate;

    private BigDecimal sumQty;

    private String memberCd;
    private String memberName;

    private String customerCd;
    private String customerName;

    private String storageCd;
    private String storageName;

    private String orderState;
    private String endYn;
    private String regYn;

}
