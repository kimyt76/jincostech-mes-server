package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* PdrWorkerView 엔티티
* </p>
*
* @author jeonsj
* @since 2023-01-19
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pdr_worker_view")
public class PdrWorkerView {


    private String pdrWorkerId;

    private String pdrSellId;

    private String pdrMasterId;

    private Integer displayOrder;

    private String gb1;

    private String gb2;

    private Integer cnt;

    private Integer workHour;

    private BigDecimal wageCost;

    private BigDecimal mealCost;

    private BigDecimal wageAmt;

    private BigDecimal mealAmt;

    private BigDecimal directCost;

}
