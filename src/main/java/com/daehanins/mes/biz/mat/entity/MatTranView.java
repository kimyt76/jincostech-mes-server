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
* MatTranView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-04-13
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_tran_view")
public class MatTranView {

    @TableId
    private String matTranId;

    private String matOrderId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate tranDate;

    private Integer serNo;

    // 자재거래번호 : "tranDate - serNo"
    private String tranNo;

    private String memo;

    private String tranCd;

    private String tranSign;

    private String tranName;

    private String memberCd;

    private String memberName;

    private String confirmMemberCd;

    private String confirmMemberName;

    private String customerCd;

    private String customerName;

    private String srcStorageCd;

    private String srcStorageName;

    private String destStorageCd;

    private String destStorageName;

    private String orderType;

    private String vatType;

    private Integer cnt;

    private BigDecimal sumQty;

    private BigDecimal sumOrderQty;

    private BigDecimal sumSupplyAmt;

    private BigDecimal sumVat;

    private String itemCdJoin;

    private String itemNameJoin;

    private String itemName;

    private String erpYn;

    private String printYn;

    private String endYn;

    private String confirmState;

    private String confirmStateName;

    private String processCd;

}
