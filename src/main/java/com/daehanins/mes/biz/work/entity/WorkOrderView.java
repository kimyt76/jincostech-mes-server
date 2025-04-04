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
import java.time.LocalDateTime;

/**
* <p>
* WorkOrderView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-04-28
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_order_view")
public class WorkOrderView {

    @TableId
    private String workOrderId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    private Integer serNo;

    private String orderNo;

    private String areaCd;

    private String areaName;

    private String customerCd;

    private String customerName;

    private String itemCd;

    private String itemName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate deliveryDate;

    private BigDecimal deliveryQty;

    private String chargerId;

    private Integer batchCnt;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;}
