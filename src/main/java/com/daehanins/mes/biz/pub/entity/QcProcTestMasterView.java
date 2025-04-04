package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * QcProcTestMasterView 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("qc_proc_test_master_view")
public class QcProcTestMasterView {

    @TableId
    private String workOrderBatchId;

    private String areaCd;

    private String areaName;

    private String orderNo;

    private Integer batchSerNo;

    private String qcProcTestMasterId;

    private String qcProcTestType;

    private String testMember;

    private String testState;

    private String memo;

    private String coatingId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate coatingDate;

    private String chargingId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate chargingDate;

    private String packingId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate packingDate;

    private String batchStatus;

    private String customerCd;

    private String customerName;

    private String itemCd;

    private String itemName;

    private String prodNo;

    private String lotNo;

    private String lotNo2;

    private String displayCapacity;

    private String chargingQtys;

    private String chargingCnt;

    private String cappingRange;

    private String essenceStd;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    private BigDecimal prodQty;

    private String workFlow;

}
