package com.daehanins.mes.biz.adm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.daehanins.mes.base.BaseEntity;

/**
* <p>
* SysLogJob 엔티티
* </p>
*
* @author jeonsj
* @since 2022-12-01
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_log_job")
public class SysLogJob extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String logJobId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String workTime;

    private Integer logDataCnt;

    private String logDt;

}
