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

import java.time.LocalDateTime;

/**
 * <p>
 * 제조진행ProdIng 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("prod_ing")
public class ProdIng extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 제조진행ID
     */
    @TableId
    private String prodIngId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 작업지시상세ID
     */
    private String workOrderItemId;

    /**
     * 제조상태
     */
    private String prodState;

    /**
     * 시작일시
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime startDatetime;

    /**
     * 종료일시
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime endDatetime;

    /**
     * 온도
     */
    private Double temperature;

    /**
     * RPM 1, 2, 3
     */
    private Double rpm1;
    private Double rpm2;
    private Double rpm3;

    /**
     * 정지사유
     */
    private String stopReason;

}
