package com.daehanins.mes.biz.pub.entity;

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
 * QcProcTestSample 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qc_proc_test_sample")
public class QcProcTestSample extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String qcProcTestSampleId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String qcProcTestMasterId;

    private String sampleDate;

    private String sampleMember;

    private Integer qty1 = 0;

    private Integer qty2 = 0;

    private Integer qty3 = 0;

    private Integer qty4 = 0;

    private Integer qty5 = 0;

    private Integer qty6 = 0;

    private Integer qty7 = 0;

    private Integer qty8 = 0;

    private Integer qty9 = 0;

}
