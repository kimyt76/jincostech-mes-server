package com.daehanins.mes.biz.pub.entity;

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
 * ProcTestWorker 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("proc_test_worker")
public class ProcTestWorker extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String procTestWorkerId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String procTestMasterId;

    private Integer displayOrder;

    private String gb;

    private String memberCd;

}
