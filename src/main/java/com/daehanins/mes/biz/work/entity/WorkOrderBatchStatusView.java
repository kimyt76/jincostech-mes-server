package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
* <p>
* WorkOrderBatchStatusView 엔티티
* </p>
*
* @author jeonsj
* @since 2022-10-30
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_order_batch_status_view")
public class WorkOrderBatchStatusView {

    private Integer serNo;

    private LocalDate orderDate;

    private String orderNo;

    private String areaCd;

    private String areaName;

    private String itemCd;

    private String itemName;

    private Integer batchSerNo;

    private String batch;

    private String prodNo;

    private String lotNo;

    private String batchStatus;

    private String batchStatusName;

    private String prodQty;

    private String coatingQty;

    private String chargingQty;

    private String packingQty;

}
