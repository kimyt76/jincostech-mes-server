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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 작업지시WorkOrder 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("work_order")
public class WorkOrder extends BaseEntity {

    private static final long serialVersionUID = 8584376147619056323L;

    /**
     * 작업지시ID
     */
    @TableId
    private String workOrderId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 연번
     */
    private Integer serNo;

    /**
     * 구역
     */
    private String areaCd;

    /**
     * 납품거래처
     */
    private String customerCd;

    /**
     * 납기일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate deliveryDate;

    private String chargerId;

    private String itemCd;

    private Double deliveryQty;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;
}
