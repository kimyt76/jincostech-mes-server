package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * WorkOrderBatch 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2021-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("work_order_batch")
public class WorkOrderBatch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String workOrderBatchId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String workOrderId;

    private Integer batchSerNo;

    private String areaCd;

    private String lotNo;

    private String lotNo2;

    private String prodNo;

}
