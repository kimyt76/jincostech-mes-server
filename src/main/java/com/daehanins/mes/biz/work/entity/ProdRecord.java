package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("prod_record")
public class ProdRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 생산실적ID
     */
    @TableId
    private String prodRecordId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 작업지시상세ID
     */
    private String workOrderItemId;

    /**
     * 제품품목코드
     */
    private String itemCd;

    /**
     * 생산(작업)일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    private String prodStartTime;

    private String prodEndTime;

    private int prodWorkerCnt;

    private BigDecimal inputQty;

    private BigDecimal prodQty;
}