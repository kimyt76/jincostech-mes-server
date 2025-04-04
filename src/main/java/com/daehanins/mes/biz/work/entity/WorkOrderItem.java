package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
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
 * 작업지시상세WorkOrderItem 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("work_order_item")
public class WorkOrderItem extends BaseEntity {

    private static final long serialVersionUID = 6029497417213704910L;

    @TableId
    private String workOrderItemId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String workOrderBatchId;

    private String processCd;

    private String storageCd;

    private String itemCd;

    private BigDecimal orderQty;

    private BigDecimal matUseQty;

    private BigDecimal prodQty;

    private BigDecimal yieldRate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate orderDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate prodDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime workStartTime;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime workEndTime;

    private String workEquipmentCd;

    private String workOrderItemStatus;

    private String resultYn = "N";

    private String endYn = "N";

    private String tranYn = "N";

    private String tranId;

    private String prodTranYn = "N";

    private String prodTranId;

    private String note;

    private String memo;

    private String itemTestNo;

    private String managerId;

    private String reqMatOrderId;

    @TableField(exist = false)
    private String rowMode;

}
