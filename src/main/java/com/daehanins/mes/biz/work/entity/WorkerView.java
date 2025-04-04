package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
* WorkerView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-10-19
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("worker_view")
public class WorkerView {

    @TableId
    private String workerId;

    private String areaCd;
    private String areaName;

    private String processCd;
    private String processName;

    private String workerName;

    private String memo;
}
