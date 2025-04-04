package com.daehanins.mes.biz.work.entity;

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
* ProdResultView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-05-25
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prod_result_view")
public class ProdResultView {

    @TableId
    private String prodResultId;

    private String workOrderItemId;

    private String workOrderId;

    private String orderNo;

    private String areaCd;

    private String areaName;

    private String memberCd;

    private String memberName;

    private String customerCd;

    private String customerName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate workDate;

    private Integer serNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate deliveryDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate ordProdDate;

    private String itemTypeCd;

    private String itemCd;

    private String itemName;

    private String lotNo;

    private String prodNo;

    private String workItemState;

    private String workItemStateName;

    private BigDecimal orderQty;

    private BigDecimal prodQty;

    private BigDecimal badQty;

    private BigDecimal unitWeight;

    private BigDecimal prodConvQty;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate expiryDate;

    private String prodTranYn;

    private String prodTranId;

    private String srcStorageCd;

    private String testNo;

    private String testState;

    private String testStateName;

    private String passState;

    private String passStateName;

    private String cosFormulaId;

    private String memo;

}
