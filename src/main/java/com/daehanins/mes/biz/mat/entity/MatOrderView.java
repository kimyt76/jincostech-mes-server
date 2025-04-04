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
* MatOrderView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-06-25
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_order_view")
public class MatOrderView {

    @TableId
    private String matOrderId;
    private String tranCd;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;
    private Integer serNo;

    // 발주번호 : "orderDate - serNo"
    private String orderNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate deliveryDate;

    private String customerCd;
    private String customerName;
    private String customerManager;
    private String maker;

    private String orderState;


    private String srcStorageCd;
    private String srcStorageName;

    private String destStorageCd;
    private String destStorageName;


    private String memberCd;
    private String memberName;

    private String itemTypeCd;
    private String itemTypeName;

    private String itemName;
    private String itemCdJoin;
    private String itemNameJoin;
    private String itemCnt;

    private String memo;

    private BigDecimal sumOrderQty;
    private BigDecimal sumSupplyAmt;
    private BigDecimal sumVat;

    private String printYn;
    private String mailYn;
    private String endYn;

    private String areaCd;


}
