package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* PdrLaborView 엔티티
* </p>
*
* @author jeonsj
* @since 2023-01-18
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pdr_labor_view")
public class PdrLaborView {


    private String pdrLaborId;

    private String pdrMasterId;

    private Integer displayOrder;

    private String gb1;

    private String gb2;

    private Integer maleCnt;

    private Integer femaleCnt;

    private Integer totalCnt;

    private Integer laborHour;

    private BigDecimal hourlyWage;

    private BigDecimal laborAmt;

    private String memo;


}
