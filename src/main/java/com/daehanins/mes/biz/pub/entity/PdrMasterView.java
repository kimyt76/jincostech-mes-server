package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
* <p>
* PdrMasterView 엔티티
* </p>
*
* @author jeonsj
* @since 2022-12-27
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pdr_master_view")
public class PdrMasterView {

    @TableId
    private String pdrMasterId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    private String areaCd;

    private String areaName;

    private String stdTime;

    private Integer m1Cost;

    private Integer m2Cost;

    private Integer directCost;

    private Integer laborCost;

    private Integer etcCost;

    private Integer totalSellAmt;

    private Integer prodCost;

    private Integer ordinaryIncome;

    private Integer profitRate;

    private String memo;

    private String regStatus;

}
