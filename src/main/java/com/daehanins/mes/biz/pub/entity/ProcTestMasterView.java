package com.daehanins.mes.biz.pub.entity;

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
* ProcTestMasterView 엔티티
* </p>
*
* @author jeonsj
* @since 2023-03-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("proc_test_master_view")
public class ProcTestMasterView {

    @TableId
    private String workOrderItemId;

    private String workOrderId;

    private String orderNo;

    private String areaCd;

    private String areaName;

    private String customerCd;

    private String customerName;

    private String workOrderBatchId;

    private Integer batchSerNo;

    private String prodNo;

    private String lotNo;

    private String processCd;

    private String processName;

    private String storageCd;

    private String storageName;

    private String itemCd;

    private String itemName;

    private String itemTypeCd;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    private BigDecimal prodQty;

    private BigDecimal producedQty;

    private BigDecimal orderQty;

    private BigDecimal matUseQty;

    private BigDecimal yieldRate;

    private String workEquipmentCd;

    private String equipmentName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime workStartTime;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime workEndTime;

    private String workOrderItemStatus;

    private String batchStatus;

    private String tranYn;

    private String tranId;

    private String prodTranYn;

    private String prodTranId;

    private String resultYn;

    private String endYn;

    private String memo;

    private String note;

    private String itemTestNo;

    private String managerId;

    private String reqMatOrderId;

    private String orderState;

    private Double prodPrice;

    private String procTestMasterId;

    private String testState;

}
