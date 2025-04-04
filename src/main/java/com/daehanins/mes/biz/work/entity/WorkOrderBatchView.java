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
* WorkOrderBatchView 엔티티
* </p>
*
* @author jeonsj
* @since 2021-02-22
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_order_batch_view")
public class WorkOrderBatchView {

    @TableId
    private String workOrderBatchId;

    private String workOrderId;

    private String customerCd;

    private String customerName;

    private LocalDate deliveryDate;

    private BigDecimal deliveryQty;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    private Integer serNo;

    private String orderNo;

    private String areaCd;

    private String areaName;

    private String itemCd;

    private String itemName;

    private Integer batchSerNo;

    private String prodNo;

    private String lotNo;

    private String lotNo2;

    private String batchStatus;

    private String batchStatusName;

    //칭량
    private String weighId;
    private String weighItemCd;
    private String weighItemName;
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate weighDate;
    private BigDecimal weighQty;
    private String weighStatus;

    //제조
    private String prodId;
    private String prodItemCd;
    private String prodItemName;
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;
    private BigDecimal prodQty;
    private String prodStatus;
    private String prodEquipmentCd;

    //코팅
    private String coatingId;
    private String coatingItemCd;
    private String coatingItemName;
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate coatingDate;
    private BigDecimal coatingQty;
    private String coatingStatus;

    //충전
    private String chargingId;
    private String chargingItemCd;
    private String chargingItemName;
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate chargingDate;
    private BigDecimal chargingQty;
    private String chargingStatus;

    //포장
    private String packingId;
    private String packingItemCd;
    private String packingItemName;
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate packingDate;
    private BigDecimal packingQty;
    private String packingStatus;

}
