package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
* WorkOrderItemView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-04-28
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_order_item_view")
public class WorkOrderItemView {

    @TableId
    private String workOrderItemId;

    /** work_order **/
    private String workOrderId;
    private String orderNo;
    private String areaCd;
    private String areaName;
    private String customerCd;
    private String customerName;

    /** work_order_batch **/
    private String workOrderBatchId;
    private Integer batchSerNo;
    private String prodNo;
    private String lotNo;
    private String lotNo2;

    /** work_order_item **/
    private String processCd;
    private String processName;
    private String storageCd;
    private String storageName;
    private String itemCd;
    private String itemName;
    private String itemTypeCd;
    private String itemTestNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    private BigDecimal orderQty;
    private BigDecimal matUseQty;
    private BigDecimal prodQty;
    private BigDecimal producedQty;

    private BigDecimal yieldRate;
    private String workEquipmentCd;
    private String equipmentName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime workStartTime;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime workEndTime;

    private String workOrderItemStatus;
    private String batchStatus;

    private String tranYn = "N";
    private String tranId;

    private String prodTranYn = "N";
    private String prodTranId;

    private String resultYn = "N";
    private String endYn = "N";

    private String note;
    private String memo;

    private String managerId;

    private String reqMatOrderId;

    private String orderState;

    private BigDecimal prodPrice;

    @TableField(exist = false)
    private String rowMode;
}
