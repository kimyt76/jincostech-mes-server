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
 * Worker 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("worker")
public class Worker extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 작업자id
     */
    @TableId
    private String workerId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 구역(공장)
     */
    private String areaCd;

    /**
     * 작업구분
     */
    private String processCd;

    /**
     * 작업자명
     */
    private String workerName;

    /**
     * 비고
     */
    private String memo;


}
