package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 구역Area 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("area")
public class Area extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 구역코드
     */
    @TableId
    private String areaCd;

    /**
     * 구역명
     */
    private String areaName;

    /**
     * 제조시설여부
     */
    private String prodYn;

    /**
     * 사내여부
     */
    private String inboundYn;

}
