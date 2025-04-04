package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * QcProcTestMaster 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qc_proc_test_master")
public class QcProcTestMaster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String qcProcTestMasterId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String workOrderBatchId;

    private String qcProcTestType;

    private String testMember;

    private String testState;

    private String memo;

}
